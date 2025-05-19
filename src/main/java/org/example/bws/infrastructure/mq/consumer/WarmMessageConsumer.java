package org.example.bws.infrastructure.mq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.model.SignalReport;
import org.example.bws.domain.model.WarnInfo;
import org.example.bws.domain.service.SignalService;
import org.example.bws.infrastructure.repository.WarnInfoRepository;
import org.example.bws.shared.event.SignalProcessedEvent;
import org.example.bws.shared.utils.DtoToModelConverter;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Component
@RocketMQMessageListener(
        consumerGroup = "bws",
        topic = "warn-message",
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.CLUSTERING,
        consumeThreadNumber = 4
)
public class WarmMessageConsumer implements RocketMQListener<String> {

    private static final String REDIS_KEY_PREFIX = "signal:processed:";
    private static final int REDIS_KEY_EXPIRE_MINUTES = 10;
    private static final Logger log = LoggerFactory.getLogger(WarmMessageConsumer.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SignalService signalService;

    @Autowired
    private WarnInfoRepository warnInfoRepository;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onMessage(String jsonMessage) {
        Long signalId = null;
        try {
            SignalReport signalReport = parseSignalReport(jsonMessage);
            signalId = signalReport.getId();

            if (signalId == null) {
                log.warn("收到无效消息，ID 为空");
                return;
            }

            String redisKey = REDIS_KEY_PREFIX + signalId;
            RBucket<String> bucket = redissonClient.getBucket(redisKey);
            boolean isFirstProcess = bucket.trySet("processed", REDIS_KEY_EXPIRE_MINUTES, TimeUnit.MINUTES);
            if (!isFirstProcess) {
                log.info("消息已处理，跳过 signalId: {}", signalId);
                return;
            }


            processSignal(signalReport);

            // 发布事件通知
            applicationEventPublisher.publishEvent(new SignalProcessedEvent(this, List.of(signalId)));

        } catch (JsonProcessingException e) {
            log.error("消息格式错误，JSON 解析失败", e);
        } catch (Exception e) {
            log.error("处理消息失败，signalId: {}", signalId, e);
            if (signalId != null) {
                String redisKey = REDIS_KEY_PREFIX + signalId;
                try {
                    redissonClient.getBucket(redisKey).delete();
                } catch (Exception ex) {
                    log.warn("删除 Redis 锁失败 key={}", redisKey, ex);
                }
            }
        }
    }

    private SignalReport parseSignalReport(String jsonMessage) throws JsonProcessingException {
        return objectMapper.readValue(jsonMessage, SignalReport.class);
    }

    private void processSignal(SignalReport signalReport) {
        List<WarnInfoDto> warnInfoDtos = signalService.handleSignalReport(signalReport);
        List<WarnInfo> warnInfos = new ArrayList<>();

        for (WarnInfoDto dto : warnInfoDtos) {
            WarnInfo warnInfo = DtoToModelConverter.convertWarnInfo(
                    dto,
                    signalReport.getCarId(),
                    signalReport.getWarnId(),
                    signalReport.getId(),
                    null
            );
            warnInfos.add(warnInfo);
            log.debug("转换后的告警信息: {}", warnInfo);
        }

        warnInfoRepository.batchInsert(warnInfos);
    }
}
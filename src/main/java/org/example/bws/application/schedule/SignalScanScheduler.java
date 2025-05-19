package org.example.bws.application.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.model.SignalReport;
import org.example.bws.domain.service.SignalService;
import org.example.bws.infrastructure.mq.producer.WarmMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电池信号扫描定时任务（应用层调度器）
 */
@Component
@Slf4j
public class SignalScanScheduler {

    private final SignalService signalService;
    private final WarmMessageProducer messageProducer;

    private final ObjectMapper objectMapper;
    private final String TOPIC ="warn-message";

    @Autowired
    public SignalScanScheduler(SignalService signalService,
                               WarmMessageProducer messageProducer,
                               @Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        this.signalService = signalService;
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
    }

//    // 每天凌晨1点执行定时扫描
//    @Scheduled(cron = "0 0 1 * * ?")
// 在应用启动后，延迟 1 秒执行第一次，之后每 24 小时执行一次
    @Scheduled(initialDelay = 1000, fixedRate = 86400000) // 86400000 毫秒 = 24 小时
    public void scanAndProduceSignals() {
        System.out.println("------------");
        try {
            // 1. 调用领域服务获取待处理的电池信号
            List<SignalReport> unprocessedSignals = signalService.getUnprocessedSignals();
            //2. 发送未处理消息
            for (SignalReport unprocessedSignal : unprocessedSignals) {
                String json = "";
                boolean flag =true  ;
                try{
                    // 序列化成 JSON 字符串
                    json= objectMapper.writeValueAsString(unprocessedSignal);
                    System.out.println(json);
                }catch (Exception e){
                    flag = false;
                    System.out.println("LOG::WARN:消息"+unprocessedSignal.getId()+"无法被序列化");
                }

              if(flag){
                  // 发送到消息队列
                  messageProducer.sendMessage(TOPIC, json);
              }
            }
        } catch (Exception e) {
            // 处理异常
            throw new RuntimeException("定时任务扫描信号失败", e);
        }
    }
}
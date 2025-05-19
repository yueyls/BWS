package org.example.bws.infrastructure.mq.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 刘仁杰
 */
@Component
public class WarmMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(WarmMessageProducer.class);
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic,String msg) {
        rocketMQTemplate.asyncSend(topic, msg,new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {
                log.warn(throwable.getMessage());
            }
        });
    }
}
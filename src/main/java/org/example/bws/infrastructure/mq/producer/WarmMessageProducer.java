package org.example.bws.infrastructure.mq.producer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 刘仁杰
 */
@Component
public class WarmMessageProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage(String topic,String msg) {
        rocketMQTemplate.syncSend(topic, "Hello World");
    }
}
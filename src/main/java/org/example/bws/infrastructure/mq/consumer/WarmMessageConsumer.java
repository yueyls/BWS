package org.example.bws.infrastructure.mq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Administrator
 */
@Component
@RocketMQMessageListener(
        consumerGroup = "bws",
        topic = "warm-message",
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel = MessageModel.BROADCASTING,
        consumeThreadNumber = 4
)
public class WarmMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(String jsonMessage) {
        System.out.println(jsonMessage);
    }
}
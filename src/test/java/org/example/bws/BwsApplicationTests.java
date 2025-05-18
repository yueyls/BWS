package org.example.bws;

import org.example.bws.infrastructure.mq.producer.WarmMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class BwsApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    WarmMessageProducer warmMessageProducer;

    @Test
    void contextLoads() {
    }

    @Test
    void redisTest(){
        redisTemplate.opsForValue().set("now","value");
    }

    @Test
    void rocketmqTest() throws InterruptedException {
        warmMessageProducer.sendMessage("warm-message","Test");
        Thread.sleep(5000);
    }





}

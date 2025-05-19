package org.example.bws;

import org.example.bws.infrastructure.mq.producer.WarmMessageProducer;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class BwsApplicationTests {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    WarmMessageProducer warmMessageProducer;

    @Test
    void contextLoads() {
    }

    @Test
    void redisTest(){
        RBucket<Object> key = redissonClient.getBucket("key");
        key.set(1);
    }

    @Test
    void rocketmqTest() throws InterruptedException {
        warmMessageProducer.sendMessage("warm-message","Test");
        Thread.sleep(5000);
    }





}

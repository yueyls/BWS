package org.example.bws;


import org.example.bws.domain.service.RuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RuleTest {

    @Autowired
    RuleService ruleService;

    @Test
    public void testRedis() {

    }
}

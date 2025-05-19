package org.example.bws.utilsTest;


import org.example.bws.domain.model.AlertRule;
import org.example.bws.domain.model.Rule;
import org.example.bws.domain.model.SignalVO;
import org.example.bws.shared.utils.RuleParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class test {

    @Test
    public void test() {
        Rule rule = new Rule(); // 假设已初始化 Rule 对象
        rule.setAlertRules(Arrays.asList(
//                new AlertRule("5 <= (Mx - Mi)", 0),
                new AlertRule("3 <= (Mx - Mi) ", 1),
                new AlertRule("0.2 <= (Ix - Ii) <1",1)
        ));

        RuleParser parser = new RuleParser();
//        int warnLevel = parser.evaluateWarningLevel(rule, 6.0, 3.0,0.0,0.0);
        AlertRule alertRule = parser.evaluateWarningLevel(rule, 0.0, 0.0, 12.0, 11.7);
        System.out.println(alertRule.getWarnLevel());
    }

}

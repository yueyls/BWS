package org.example.bws.mapper;


import org.example.bws.domain.enums.BatteryType;
import org.example.bws.domain.model.AlertRule;
import org.example.bws.domain.model.Rule;
import org.example.bws.infrastructure.repository.RuleRepository;
import org.example.bws.infrastructure.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.example.bws.domain.enums.BatteryType.LITHIUM_NICKEL_MANGANESE_COBALT;

@SpringBootTest
public class rule {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Test
    void test(){
        Rule rule = ruleRepository.selectByRuleId(1);
        System.out.println(rule.getRuleName());
        System.out.println(rule.getBatteryType());
        List<AlertRule> alertRules = rule.getAlertRules();
        alertRules.forEach((r)-> System.out.println(r.getCondition()));
    }


    @Test
    void testVehicle(){
        BatteryType batteryType = vehicleRepository.selectBatteryTypeByCarId(1);
        System.out.println(batteryType);
    }

    @Test
    void test2(){
        List<Rule> rules = ruleRepository.selectByBatteryType(LITHIUM_NICKEL_MANGANESE_COBALT);
        System.out.println(rules.size());
    }
}

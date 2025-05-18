package org.example.bws.domain.service;


import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.enums.BatteryType;
import org.example.bws.domain.model.AlertRule;
import org.example.bws.domain.model.Rule;
import org.example.bws.domain.model.SignalVO;
import org.example.bws.infrastructure.repository.RuleRepository;
import org.example.bws.shared.utils.RuleParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {


    @Autowired
    RedisTemplate redisTemplate;


    @Autowired
    RuleRepository ruleRepository;



    //TODO 从缓存获取 规则
    public List<WarnInfoDto> handlerRule( int warnId,BatteryType batteryType, SignalVO signalVO){
        RuleParser ruleParser = new RuleParser();
        List<WarnInfoDto> ret=new ArrayList<>();
        List<Rule> rules ;
        if(warnId==0){
            rules= ruleRepository.selectByBatteryType(batteryType);
            System.out.println(rules.size());
        }else{
            rules=ruleRepository.selectByRuleId(warnId,batteryType);
            System.out.println(rules.size());
        }
        for (Rule rule : rules) {
            WarnInfoDto tmp=new WarnInfoDto();
            AlertRule alertRule = ruleParser.evaluateWarningLevel(rule, signalVO);
            if(alertRule.getWarnLevel()!=-1){
                tmp.setWarnLevel(alertRule.getWarnLevel());
                tmp.setBatteryType(rule.getBatteryType());
                tmp.setWarnName(rule.getRuleName());
                ret.add(tmp);
            }
        }
        return ret;
    }


}

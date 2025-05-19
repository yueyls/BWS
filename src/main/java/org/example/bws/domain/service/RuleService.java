package org.example.bws.domain.service;

import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.enums.BatteryType;
import org.example.bws.domain.model.AlertRule;
import org.example.bws.domain.model.Rule;
import org.example.bws.domain.model.SignalVO;
import org.example.bws.infrastructure.repository.RuleRepository;
import org.example.bws.shared.utils.RuleParser;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RuleService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RuleRepository ruleRepository;

    // Redis key 前缀
    private static final String RULE_CACHE_KEY_PREFIX = "rule:cache:";

    public List<WarnInfoDto> handlerRule(int warnId, BatteryType batteryType, SignalVO signalVO) {
        RuleParser ruleParser = new RuleParser();
        List<WarnInfoDto> ret = new ArrayList<>();

        List<Rule> rules = getRulesFromCacheOrDB(warnId, batteryType);

        for (Rule rule : rules) {
            WarnInfoDto tmp = new WarnInfoDto();
            AlertRule alertRule = ruleParser.evaluateWarningLevel(rule, signalVO);
            if (alertRule==null){
                continue;
            }
            tmp.setWarnLevel(alertRule.getWarnLevel());
            tmp.setBatteryType(rule.getBatteryType());
            tmp.setWarnName(rule.getRuleName());
            ret.add(tmp);
        }

        return ret;
    }

    private List<Rule> getRulesFromCacheOrDB(int warnId, BatteryType batteryType) {
        String cacheKey = buildCacheKey(warnId, batteryType);

        RBucket<List<Rule>> bucket = redissonClient.getBucket(cacheKey);
        List<Rule> cachedRules = bucket.get();

        if (cachedRules != null && !cachedRules.isEmpty()) {
            System.out.println("cache");
            for (Rule dbRule : cachedRules) {
                System.out.println(dbRule);
            }
            return cachedRules;
        }

        System.out.println("DB");
        // 缓存为空，则从数据库加载
        List<Rule> dbRules;
        if (warnId == 0) {
            dbRules = ruleRepository.selectByBatteryType(batteryType);
        } else {
            dbRules = ruleRepository.selectByRuleId(warnId, batteryType);
        }

        for (Rule dbRule : dbRules) {
            System.out.println(dbRule);
        }
        // 如果数据库也无数据，返回空列表避免频繁查库
        if (dbRules == null || dbRules.isEmpty()) {
            return Collections.emptyList();
        }

        // 写入 Redis 缓存
        bucket.set(dbRules);

        return dbRules;
    }

    private String buildCacheKey(int warnId, BatteryType batteryType) {
        return RULE_CACHE_KEY_PREFIX + batteryType.getValue() + ":" + warnId;
    }
}
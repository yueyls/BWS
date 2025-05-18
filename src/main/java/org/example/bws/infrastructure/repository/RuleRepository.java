package org.example.bws.infrastructure.repository;


import org.apache.ibatis.annotations.Mapper;
import org.example.bws.domain.model.Rule;

import java.util.List;

@Mapper
public interface RuleRepository {
    /**
     * 根据规则编号(rule_id)查询单个规则
     * @param ruleId 规则编号（如1: 电压差报警）
     * @return 匹配的规则对象
     */
    Rule selectByRuleId(int ruleId);

    /**
     * 根据电池类型查询规则列表
     * @param batteryType 电池类型枚举（BatteryType）
     * @return 符合条件的规则列表
     */
    List<Rule> selectByBatteryType(org.example.bws.domain.enums.BatteryType batteryType);
}

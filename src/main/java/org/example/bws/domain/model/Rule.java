package org.example.bws.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.bws.domain.enums.BatteryType;
import org.example.bws.domain.model.AlertRule;

import java.util.List;


public class Rule {


    private int id;
    private int ruleId;
    private String ruleName;
    @JsonProperty("battery_type")
    private BatteryType batteryType;
    @JsonProperty("alert_rules")
    private List<AlertRule> alertRules;

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", batteryType=" + batteryType +
                ", alertRules=" + alertRules +
                '}';
    }

    public Rule() {}

    public Rule(int id,int ruleId, String ruleName, BatteryType batteryType, List<AlertRule> alertRules) {
        this.id = id;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.batteryType = batteryType;
        this.alertRules = alertRules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public BatteryType getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(BatteryType batteryType) {
        this.batteryType = batteryType;
    }

    public List<AlertRule> getAlertRules() {
        return alertRules;
    }

    public void setAlertRules(List<AlertRule> alertRules) {
        this.alertRules = alertRules;
    }
}
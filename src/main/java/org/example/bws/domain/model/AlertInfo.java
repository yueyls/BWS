package org.example.bws.domain.model;

import lombok.Data;
import org.example.bws.domain.enums.BatteryType;

import java.time.LocalDateTime;


@Data
public class AlertInfo {
    private Long id;
    private Long reportId;
    private BatteryType batteryType;
    private int ruleId;
    private String ruleName;
    private int alertLevel;
    private LocalDateTime alertTime;

    public AlertInfo(Long id, Long reportId, BatteryType batteryType, int ruleId, String ruleName, int alertLevel, LocalDateTime alertTime) {
        this.id = id;
        this.reportId = reportId;
        this.batteryType = batteryType;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.alertLevel = alertLevel;
        this.alertTime = alertTime;
    }
    public AlertInfo(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public BatteryType getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(BatteryType batteryType) {
        this.batteryType = batteryType;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(int alertLevel) {
        this.alertLevel = alertLevel;
    }

    public LocalDateTime getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(LocalDateTime alertTime) {
        this.alertTime = alertTime;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}

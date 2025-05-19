package org.example.bws.domain.model;


import org.example.bws.domain.enums.BatteryType;
import java.time.LocalDateTime;


/**
 * @author Administrator
 */
public class WarnInfo {
    private Long id;
    private Long reportId;
    private int carId;
    private BatteryType batteryType;
    private int ruleId;
    private String ruleName;
    private int warnLevel;
    private LocalDateTime warnTime;


    public WarnInfo(Long id, Long reportId, int carId, BatteryType batteryType, int ruleId, String ruleName, int warnLevel, LocalDateTime warnTime) {
        this.id = id;
        this.reportId = reportId;
        this.carId = carId;
        this.batteryType = batteryType;
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.warnLevel = warnLevel;
        this.warnTime = warnTime;
    }

    @Override
    public String toString() {
        return "WarnInfo{" +
                "id=" + id +
                ", reportId=" + reportId +
                ", carId=" + carId +
                ", batteryType=" + batteryType +
                ", ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", warnLevel=" + warnLevel +
                ", warnTime=" + warnTime +
                '}';
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

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public BatteryType getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(BatteryType batteryType) {
        this.batteryType = batteryType;
    }

    public int getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(int warnLevel) {
        this.warnLevel = warnLevel;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public LocalDateTime getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(LocalDateTime warnTime) {
        this.warnTime = warnTime;
    }
}
package org.example.bws.domain.model;

public class AlertRule {
    private String condition;
    private int warnLevel;

    public AlertRule() {}

    public AlertRule(String condition, int warnLevel) {
        this.condition = condition;
        this.warnLevel = warnLevel;
    }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public int getWarnLevel() { return warnLevel; }
    public void setWarnLevel(int warnLevel) { this.warnLevel = warnLevel; }
}
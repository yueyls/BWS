package org.example.bws.domain.dto;

import org.example.bws.domain.enums.BatteryType;

public class WarnInfoDto {

    BatteryType batteryType;
    String warnName;
    int warnLevel;

    public WarnInfoDto(String warnName, int warnLevel, BatteryType batteryType) {
        this.warnName = warnName;
        this.warnLevel = warnLevel;
        this.batteryType = batteryType;
    }

    public WarnInfoDto() {

    }

    public int getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(int warnLevel) {
        this.warnLevel = warnLevel;
    }

    public BatteryType getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(BatteryType batteryType) {
        this.batteryType = batteryType;
    }

    public String getWarnName() {
        return warnName;
    }

    public void setWarnName(String warnName) {
        this.warnName = warnName;
    }




}

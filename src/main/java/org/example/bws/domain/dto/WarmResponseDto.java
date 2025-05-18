package org.example.bws.domain.dto;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.bws.domain.enums.BatteryType;

@Data
@Getter
@Setter
public class WarmResponseDto {
    private int carId;
    private BatteryType batteryType;
    private String warnName;
    private int warnLevel;

    public WarmResponseDto(int carId, WarnInfoDto warnInfoDto){
        this.carId = carId;
        this.batteryType=warnInfoDto.getBatteryType();
        this.warnName = warnInfoDto.getWarnName();
        this.warnLevel = warnInfoDto.getWarnLevel();
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getWarnName() {
        return warnName;
    }

    public void setWarnName(String warnName) {
        this.warnName = warnName;
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
}

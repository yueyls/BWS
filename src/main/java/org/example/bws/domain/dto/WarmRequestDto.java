package org.example.bws.domain.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Data
public class WarmRequestDto {

    @NotNull
    private int carId;

    private int warnId;

    @NotNull
    private String signal;

    @NotNull
    public int getCarId() {
        return carId;
    }

    public void setCarId(@NotNull int carId) {
        this.carId = carId;
    }

    public int getWarnId() {
        return warnId;
    }

    public void setWarnId(int warnId) {
        this.warnId = warnId;
    }

    public @NotNull String getSignal() {
        return signal;
    }

    public void setSignal(@NotNull String signal) {
        this.signal = signal;
    }
}

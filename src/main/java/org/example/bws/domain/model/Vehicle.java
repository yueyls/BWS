package org.example.bws.domain.model;


import lombok.Data;
import org.example.bws.domain.enums.BatteryType;

@Data
public class Vehicle {
    String vid;
    int carId;
    BatteryType batteryType;
    int totalMileage;
    int healthStatus;
}

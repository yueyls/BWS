package org.example.bws.infrastructure.repository;


import org.apache.ibatis.annotations.Mapper;
import org.example.bws.domain.enums.BatteryType;

@Mapper
public interface VehicleRepository {
    /**
     * 根据车架编号(carId)查询电池类型
     * @param carId 车架编号（业务唯一标识）
     * @return 电池类型枚举（BatteryType）
     */
    BatteryType selectBatteryTypeByCarId(int carId);
}


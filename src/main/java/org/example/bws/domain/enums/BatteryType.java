package org.example.bws.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;


public enum BatteryType {
    //有三元电池和铁锂电池两种类型
    LITHIUM_NICKEL_MANGANESE_COBALT(0,"三元电池"),
    LITHIUM_IRON_PHOSPHATE(1,"铁锂电池");



    private final int value;
    private final String desc;

    BatteryType(int value,String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    @JsonValue
    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
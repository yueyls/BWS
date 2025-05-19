package org.example.bws.domain.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum AlertLevel {
    //
    LEVEL_0(0, "最高响应"),
    LEVEL_1(1, "高"),
    LEVEL_2(2, "中"),
    LEVEL_3(3, "低"),
    LEVEL_4(4, "轻微"),
    NO_ALERT(-1, "不报警");

    private final int code;
    private final String desc;

    AlertLevel(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonValue
    public int getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
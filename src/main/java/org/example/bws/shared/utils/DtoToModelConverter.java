package org.example.bws.shared.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bws.domain.dto.WarmRequestDto;
import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.enums.BatteryType;
import org.example.bws.domain.model.SignalReport;
import org.example.bws.domain.model.SignalVO;
import org.example.bws.domain.model.WarnInfo;

import java.time.LocalDateTime;

public class DtoToModelConverter {

    // 已有的 convert 方法
    public static SignalReport convert(WarmRequestDto dto) {
        SignalVO signalVO = JsonUtils.parseSignalToVO(dto.getSignal());
        System.out.println(signalVO);
        return new SignalReport(
                null,
                dto.getWarnId(),
                dto.getCarId(),
                LocalDateTime.now(),
                signalVO,
                false
        );
    }

    /**
     * 将 WarnInfoDto 转换为 WarnInfo
     *
     * @param dto       来源 DTO
     * @param carId     必填参数
     * @param ruleId    必填参数
     * @param reportId  可选参数
     * @param warnTime  可选时间，默认当前时间
     * @return          转换后的 WarnInfo 对象
     */
    public static WarnInfo convertWarnInfo(WarnInfoDto dto, int carId, int ruleId, Long reportId, LocalDateTime warnTime) {
        if (dto == null) {
            return null;
        }

        return new WarnInfo(
                null,
                reportId,
                carId,
                dto.getBatteryType(),
                ruleId,
                dto.getWarnName(),
                dto.getWarnLevel(),
                warnTime != null ? warnTime : LocalDateTime.now()
        );
    }

    /**
     * 快捷转换方法，使用默认值填充可选字段
     */
    public static WarnInfo convertWarnInfo(WarnInfoDto dto, int carId, int ruleId) {
        return convertWarnInfo(dto, carId, ruleId, null, null);
    }

    public static WarnInfoDto convertToDto(WarnInfo warnInfo) {
        if (warnInfo == null) {
            return null;
        }

        WarnInfoDto dto = new WarnInfoDto();
        dto.setBatteryType(warnInfo.getBatteryType());
        dto.setWarnName(warnInfo.getRuleName());
        dto.setWarnLevel(warnInfo.getWarnLevel());

        return dto;
    }
}
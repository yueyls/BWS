package org.example.bws.domain.service;


import org.example.bws.domain.dto.WarmRequestDto;
import org.example.bws.domain.dto.WarmResponseDto;
import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.enums.BatteryType;
import org.example.bws.domain.model.AlertRule;
import org.example.bws.domain.model.SignalReport;
import org.example.bws.domain.model.SignalVO;
import org.example.bws.infrastructure.repository.SignalReportRepository;
import org.example.bws.infrastructure.repository.VehicleRepository;
import org.example.bws.shared.utils.DtoToModelConverter;
import org.example.bws.shared.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SignalService {

    @Autowired
    RuleService ruleService;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    SignalReportRepository signalReportRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(List<WarmRequestDto> warmRequestDtos){
        List<SignalReport> signalReports=new ArrayList<>();
        for (WarmRequestDto warmRequestDto : warmRequestDtos) {
            SignalReport convert = DtoToModelConverter.convert(warmRequestDto);
            signalReports.add(convert);
        }
        signalReportRepository.batchInsert(signalReports);
    }



    public void handleSignalReport(List<SignalReport> reports){
        for (SignalReport report : reports) {

        }
    }

    public void handleWarmSignal(List<WarmRequestDto> requestDtos){
        for (WarmRequestDto requestDto : requestDtos) {
            handleWarmSignal(requestDto);
        }
    }

    private void handleWarmSignal(WarmRequestDto requestDto){
        //获取电池类型
        BatteryType batteryType = vehicleRepository.selectBatteryTypeByCarId(requestDto.getCarId());
        //反序列化获取便于RuleService处理的SignalVO
        SignalVO signalVO = JsonUtils.parseSignalToVO(requestDto.getSignal());
        //处理信号
        int ruleId=requestDto.getWarnId();

        if (ruleId != 0) {
            WarnInfoDto warnInfoDto = ruleService.handlerSingleRule(ruleId, batteryType, signalVO);
            if (warnInfoDto == null) {
                throw new RuntimeException("没有任何的规则匹配");
            }
        }else{
            List<WarnInfoDto> warnInfoDtos=ruleService.handlerRule(batteryType,signalVO);
            if (warnInfoDtos.isEmpty()){
                throw new RuntimeException("没有任何的规则匹配");
            }
        }
    }
}

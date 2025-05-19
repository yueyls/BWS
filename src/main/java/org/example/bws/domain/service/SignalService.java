package org.example.bws.domain.service;


import org.example.bws.domain.dto.WarmRequestDto;
import org.example.bws.domain.dto.WarmResponseDto;
import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.enums.BatteryType;
import org.example.bws.domain.model.AlertRule;
import org.example.bws.domain.model.SignalReport;
import org.example.bws.domain.model.SignalVO;
import org.example.bws.domain.model.WarnInfo;
import org.example.bws.infrastructure.repository.SignalReportRepository;
import org.example.bws.infrastructure.repository.VehicleRepository;
import org.example.bws.infrastructure.repository.WarnInfoRepository;
import org.example.bws.shared.utils.DtoToModelConverter;
import org.example.bws.shared.utils.JsonUtils;
import org.redisson.api.RedissonClient;
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

    @Autowired
    RedissonClient redissonClient;


    @Autowired
    WarnInfoRepository warnInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(List<WarmRequestDto> warmRequestDtos){
        List<SignalReport> signalReports=new ArrayList<>();
        for (WarmRequestDto warmRequestDto : warmRequestDtos) {
            SignalReport convert = DtoToModelConverter.convert(warmRequestDto);
            System.out.println(convert);
            signalReports.add(convert);
        }
        signalReportRepository.batchInsert(signalReports);
    }


    public List<SignalReport> getUnprocessedSignals(){
        return  signalReportRepository.selectByHandledStatus(false);
    }

    public void markSignalsAsProcessed(List<Long> signalIds){
        signalReportRepository.markAllAsHandled(signalIds);
    }


    public List<WarnInfoDto> handleSignalReport(List<SignalReport> reports){
        List<WarnInfoDto> ret=new ArrayList<>();
        for (SignalReport report : reports) {
            List<WarnInfoDto> warnInfoDtos = handleSignalReport(report);
            for (WarnInfoDto warnInfoDto : warnInfoDtos) {
                ret.add(warnInfoDto);
            }
        }

        return ret;
    }

    public  List<WarnInfoDto> handleSignalReport(SignalReport signalReport){
        //获取电池类型
        BatteryType batteryType = vehicleRepository.selectBatteryTypeByCarId(signalReport.getCarId());
        //处理信号
        int ruleId=signalReport.getWarnId();

        List<WarnInfoDto> warnInfoDtos=ruleService.handlerRule(ruleId,batteryType,signalReport.getSignalData());
//        if (warnInfoDtos.isEmpty()){
//            throw new RuntimeException("没有任何的规则匹配");
//        }

        System.out.println(warnInfoDtos.size());
        return warnInfoDtos;
    }



    public List<WarnInfoDto> handleWarmSignal(List<WarmRequestDto> requestDtos){
        List<WarnInfoDto> ret=new ArrayList<>();
        for (WarmRequestDto requestDto : requestDtos) {
            List<WarnInfoDto> warnInfoDtos = handleWarmSignal(requestDto);
            for (WarnInfoDto warnInfoDto : warnInfoDtos) {
                ret.add(warnInfoDto);
            }
        }

        return ret;
    }

    private List<WarnInfoDto> handleWarmSignal(WarmRequestDto requestDto){
        //获取电池类型
        BatteryType batteryType = vehicleRepository.selectBatteryTypeByCarId(requestDto.getCarId());
        //反序列化获取便于RuleService处理的SignalVO
        SignalVO signalVO = JsonUtils.parseSignalToVO(requestDto.getSignal());
        //处理信号
        int ruleId=requestDto.getWarnId();

        List<WarnInfoDto> warnInfoDtos=ruleService.handlerRule(ruleId,batteryType,signalVO);
        if (warnInfoDtos.isEmpty()){
            throw new RuntimeException("没有任何的规则匹配");
        }

        return  warnInfoDtos;
    }

}

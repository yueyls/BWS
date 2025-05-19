package org.example.bws.controller;


import org.example.bws.domain.dto.WarmRequestDto;
import org.example.bws.domain.dto.WarmResponseDto;
import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.service.SignalService;
import org.example.bws.domain.service.WarnInfoService;
import org.example.bws.shared.common.Result;
import org.example.bws.shared.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘仁杰
 */
@RestController
@RequestMapping(("/api/warm"))
public class BatteryController {

    @Autowired
    SignalService signalService;

    @Autowired
    WarnInfoService warnInfoService;


    @PostMapping("/upload")
    public Result<?> handlerWarm(@RequestBody @Valid List<WarmRequestDto> warmRequests) {
        signalService.save(warmRequests);
        return Result.success();
    }

    @PostMapping("/test")
    public Result<?> handlerWarmTest(@RequestBody @Valid List<WarmRequestDto> warmRequests) {
        List<WarnInfoDto> r = signalService.handleWarmSignal(warmRequests);
        return Result.success(r);
    }

    @GetMapping("/query")
    public Result<?> handlerWarmQuery(@RequestParam int carId) {
        List<WarnInfoDto> byCarId = warnInfoService.findByCarId(carId);
        return Result.success(byCarId);
    }

//    @GetMapping("/querysignal")
//    public Result<?> query

}

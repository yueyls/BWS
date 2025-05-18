package org.example.bws.controller;


import org.example.bws.domain.dto.WarmRequestDto;
import org.example.bws.domain.dto.WarmResponseDto;
import org.example.bws.domain.service.SignalService;
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

    @PostMapping("/upload")
    public Result<?> handlerWarm(@RequestBody @Valid List<WarmRequestDto> warmRequests) {
        signalService.save(warmRequests);
        return Result.success();
    }

}

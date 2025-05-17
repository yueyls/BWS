package org.example.bws.domain.controller;


import org.example.bws.domain.dto.WarmRequestDto;
import org.example.bws.domain.dto.WarmResponseDto;
import org.example.bws.shared.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/api"))
public class BatteryController {

    @PostMapping("/warm")
    public Result<WarmResponseDto> handlerWarm(@RequestParam List<WarmRequestDto> warmRequests) {

        return Result.success(new WarmResponseDto());
    }

}

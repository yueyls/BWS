package org.example.bws.controller;


import org.example.bws.domain.model.Rule;
import org.example.bws.domain.service.RuleService;
import org.example.bws.shared.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rule")
public class RuleController {

    @Autowired
    RuleService ruleService;

    @PostMapping("/insert")
    public Result<?> insert(@RequestBody Rule rule) {
        ruleService.insertRule(rule);
        return Result.success();
    }
}

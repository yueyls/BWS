package org.example.bws.shared.utils;

import org.example.bws.domain.model.AlertRule;
import org.example.bws.domain.model.Rule;
import org.example.bws.domain.model.SignalVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RuleParser {
    private final ExpressionEvaluator evaluator;

    public RuleParser() {
        this.evaluator = new ExpressionEvaluator();
    }

    public AlertRule evaluateWarningLevel(Rule rule, double mx, double mi, double ix, double ii) {
        Map<String, Double> variables = new HashMap<>();
        variables.put("Mx", mx);
        variables.put("Mi", mi);
        variables.put("Ix", ix);
        variables.put("Ii", ii);

        return getAlertRule(rule, variables);
    }

    public AlertRule evaluateWarningLevel(Rule rule, SignalVO signalVO) {
        signalVO.getMi();
        signalVO.getMx();
        Map<String, Double> variables = new HashMap<>();
        variables.put("Mx", signalVO.getMx());
        variables.put("Mi", signalVO.getMi());
        variables.put("Ix", signalVO.getIx());
        variables.put("Ii", signalVO.getIi());



        return getAlertRule(rule, variables);
    }

    private AlertRule getAlertRule(Rule rule, Map<String, Double> variables) {
        for (AlertRule alertRule : rule.getAlertRules()) {
            try {
                String condition = alertRule.getCondition().replaceAll("\\s+", "");
                List<String> usedVariables = extractVariables(condition);

                System.out.println(usedVariables);
                // 检查是否有变量的值为0
                boolean shouldSkip = usedVariables.stream()
                        .anyMatch(var -> {
                            Double value = variables.get(var);
                            return (value == null || value == 0.0);
                        });

                //跳过
                if (shouldSkip) {
                    continue;
                }

                if (evaluator.evaluate(alertRule.getCondition(), variables)) {
                    return alertRule;
                }
            } catch (Exception e) {
                throw new RuntimeException("规则解析失败: " + alertRule.getCondition()+ e.getMessage());
            }
        }
        return null;
    }


    private List<String> extractVariables(String condition) {
        List<String> variables = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Za-z]+");
        Matcher matcher = pattern.matcher(condition);
        while (matcher.find()) {
            String var = matcher.group();
            variables.add(var);
        }
        return variables.stream().distinct().collect(Collectors.toList());
    }
}
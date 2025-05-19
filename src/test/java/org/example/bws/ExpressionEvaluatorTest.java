package org.example.bws;

import org.example.bws.shared.utils.ExpressionEvaluator;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {

    @Test
    void evaluateSimpleArithmeticExpression() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);
        variables.put("y", 3.0);

        boolean result = evaluator.evaluate("x + y > 7", variables);
        assertTrue(result, "5+3=8 >7 应返回true");

        result = evaluator.evaluate("x * y < 16", variables);
        assertTrue(result, "5*3=15 <16 应返回true");
    }

    @Test
    void evaluateExpressionWithParentheses() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Map<String, Double> variables = new HashMap<>();
        variables.put("a", 2.0);
        variables.put("b", 3.0);
        variables.put("c", 4.0);

        boolean result = evaluator.evaluate("(a + b) * c == 20", variables);
        assertTrue(result, "(2+3)*4=20 应返回true");

        result = evaluator.evaluate("a + (b * c) > 14", variables);
        assertFalse(result, "2+(3*4)=14 >14 应返回false");
    }

    @Test
    void evaluateWithMissingVariable() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);

        assertThrows(RuntimeException.class, () -> {
            evaluator.evaluate("x + y > 10", variables);
        }, "缺少变量y应抛出异常");
    }

    @Test
    void evaluateInvalidExpression() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);

        assertThrows(RuntimeException.class, () -> {
            evaluator.evaluate("x + > 10", variables);
        }, "无效表达式应抛出异常");

        assertThrows(RuntimeException.class, () -> {
            evaluator.evaluate("x + y *", variables);
        }, "不完整表达式应抛出异常");
    }
}    
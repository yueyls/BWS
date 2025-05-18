package org.example.bws.shared.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEvaluator {

    public boolean evaluate(String expression, Map<String, Double> variables) {
        // 移除所有空格
        String expr = expression.replaceAll("\\s+", "");
        return evaluateComparison(expr, variables);
    }

    private boolean evaluateComparison(String expr, Map<String, Double> variables) {
        String[] parts = splitExpression(expr);
        System.out.println("表达式：");
        Arrays.stream(parts).forEach(System.out::print);
        System.out.println("");
        if (parts.length < 3 || parts.length % 2 == 0) {
            throw new IllegalArgumentException("无效比较表达式: " + expr);
        }

        String prevOperand = parts[0];
        for (int i = 1; i < parts.length; i += 2) {
            String op = parts[i];
            String nextOperand = parts[i + 1];

            if (!isOperator(op)) {
                throw new IllegalArgumentException("不支持的运算符: " + op);
            }

            double leftVal = evaluateOperand(prevOperand, variables);
            double rightVal = evaluateOperand(nextOperand, variables);

            if (!compare(leftVal, rightVal, op)) {
                return false;
            }
            prevOperand = nextOperand;
        }
        return true;
    }

    private String[] splitExpression(String expr) {
        Pattern pattern = Pattern.compile("(<=|>=|<|>)|([a-zA-Z0-9.()+\\-*/]+|\\([^()]+\\))");
        Matcher matcher = pattern.matcher(expr);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String group = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            sb.append(group).append(" ");
        }

        return sb.toString().trim().split("\\s+");
    }

    private boolean isOperator(String part) {
        return part.matches("<=|>=|<|>");
    }

    private boolean compare(double left, double right, String op) {
        switch (op) {
            case "<=": return left <= right;
            case ">=": return left >= right;
            case "<":  return left < right;
            case ">":  return left > right;
            default:   throw new IllegalArgumentException("不支持的运算符: " + op);
        }
    }

    private double evaluateOperand(String operand, Map<String, Double> variables) {
        if (operand.startsWith("(")) {
            String innerExpr = operand.substring(1, operand.length() - 1);
            return evaluateArithmetic(innerExpr, variables);
        }
        if (operand.matches("[a-zA-Z]+")) {
            return variables.getOrDefault(operand, 0.0);
        }
        return evaluateArithmetic(operand, variables);
    }
    private double evaluateArithmetic(String expr, Map<String, Double> variables) {
        return parseAddSub(expr, 0, expr.length() - 1, variables);
    }


//    private double parseAddSub(String expr, int start, int end, Map<String, Double> vars) {
//        double value = parseMulDiv(expr, start, end, vars);
//        for (int i = start; i <= end; i++) {
//            char c = expr.charAt(i);
//            if (c == '+' || c == '-') {
//                double rhs = parseMulDiv(expr, i + 1, end, vars);
//                value = (c == '+') ? value + rhs : value - rhs;
//                i = findOperatorEnd(expr, i); // 跳过已处理的部分
//            }
//        }
//        return value;
//    }
//
//    private double parseMulDiv(String expr, int start, int end, Map<String, Double> vars) {
//        double value = parsePrimary(expr, start, end, vars);
//        for (int i = start; i <= end; i++) {
//            char c = expr.charAt(i);
//            if (c == '*' || c == '/') {
//                double rhs = parsePrimary(expr, i + 1, end, vars);
//                value = (c == '*') ? value * rhs : value / rhs;
//                i = findOperatorEnd(expr, i);
//            }
//        }
//        return value;
//    }
//
//    private double parsePrimary(String expr, int start, int end, Map<String, Double> vars) {
//        if (Character.isDigit(expr.charAt(start))) {
//            return Double.parseDouble(expr.substring(start, end + 1));
//        }
//        if (expr.charAt(start) == '(') {
//            return parseAddSub(expr, start + 1, end - 1, vars); // 去掉括号递归解析
//        }
//        return vars.getOrDefault(expr.substring(start, end + 1), 0.0);
//    }

    private double parseAddSub(String expr, int start, int end, Map<String, Double> vars) {
        double value = parseMulDiv(expr, start, end, vars);
        for (int i = start; i <= end; i++) {
            char c = expr.charAt(i);
            if (c == '+' || c == '-') {
                // 关键点：左右操作数需分段解析
                double left = parseMulDiv(expr, start, i - 1, vars);
                double right = parseMulDiv(expr, i + 1, end, vars);
                value = (c == '+') ? left + right : left - right;
                break; // 单次运算后退出
            }
        }
        return value;
    }

    private double parseMulDiv(String expr, int start, int end, Map<String, Double> vars) {
        double value = parsePrimary(expr, start, end, vars);
        for (int i = start; i <= end; i++) {
            char c = expr.charAt(i);
            if (c == '*' || c == '/') {
                double left = parsePrimary(expr, start, i - 1, vars);
                double right = parsePrimary(expr, i + 1, end, vars);
                value = (c == '*') ? left * right : left / right;
                break; // 单次运算后退出
            }
        }
        return value;
    }

    private double parsePrimary(String expr, int start, int end, Map<String, Double> vars) {
        if (start > end) {
            return 0.0;
        }
        if (expr.charAt(start) == '(') {
            return parseAddSub(expr, start + 1, end - 1, vars);
        }

        // 查找完整变量名或数字
        for (int i = start; i <= end; i++) {
            char c = expr.charAt(i);
            if (isOperatorChar(c)) {
                // 如果是运算符，则左侧为变量或数字
                String varName = expr.substring(start, i);
                return vars.getOrDefault(varName, 0.0);
            }
        }

        // 无运算符则整体解析
        String varName = expr.substring(start, end + 1);
        if (varName.matches("\\d+(\\.\\d+)?")) {
            return Double.parseDouble(varName);
        }
        return vars.getOrDefault(varName, 0.0);
    }

    private int findNextOperator(String expr, int pos) {
        pos++;
        while (pos < expr.length() && !isOperatorChar(expr.charAt(pos))) {
            pos++;
        }
        return pos - 1;
    }

    private boolean isOperatorChar(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}
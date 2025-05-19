package org.example.bws.shared.advice;


import org.example.bws.shared.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  全局异常处理器
 * </p>
 *
 * @author 刘仁杰
 * @since 2025-05-17
 */

// 全局异常处理类
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        //result 中封装了所有错误信息
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError error : errors) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            map.put(field, message);

        }
        return Result.failure(400, "参数校验错误", map);
    }



    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException ex) {
        return Result.failure(500, "运行时异常", ex.getMessage());
    }

    // 处理所有其他异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleGlobalException(Exception ex) {
        log.info(ex.getMessage());
        return Result.failure(500,"服务器内部异常",ex.getMessage());
    }


}
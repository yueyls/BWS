package org.example.bws.shared.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bws.domain.model.SignalVO;


public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static SignalVO parseSignalToVO(String signalJson) {
        try {
            return objectMapper.readValue(signalJson, SignalVO.class);
        } catch (JsonProcessingException e) {
            // 解析错误（JSON格式问题）
            throw new RuntimeException("信号JSON格式错误: " + signalJson + "，原因: " + e.getMessage(), e);
        } catch (Exception e) {
            // 其他错误（如字段类型不匹配）
            throw new RuntimeException("信号解析失败: " + signalJson + "，原因: " + e.getMessage(), e);
        }
    }
}
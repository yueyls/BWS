package org.example.bws.shared.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.bws.domain.model.SignalVO;

public class SignalVOTypeHandler extends JacksonTypeHandler<SignalVO> {

    private static final ObjectMapper CUSTOM_MAPPER = new ObjectMapper();

    static {
        // 忽略未知字段（避免因额外字段导致反序列化失败）
        CUSTOM_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 只使用 @JsonProperty 注解的字段，不自动检测 getter/setter
        CUSTOM_MAPPER.setVisibility(
                CUSTOM_MAPPER.getVisibilityChecker()
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
        );

        // 不将日期写成时间戳
        CUSTOM_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略 null 值字段
        CUSTOM_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 支持 Java 8 的 LocalDateTime 等时间类型
        CUSTOM_MAPPER.registerModule(new JavaTimeModule());
    }

    public SignalVOTypeHandler() {
        super(SignalVO.class, CUSTOM_MAPPER);
    }
}
package org.example.bws.shared.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CustomJacksonConfig {

    @Bean("customObjectMapper")
    public ObjectMapper customObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 忽略未知字段，避免反序列化失败
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 仅使用 @JsonProperty 定义的字段，不自动检测 getter/setter
        mapper.setVisibility(mapper.getVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE));

        // 忽略 null 值字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 支持 LocalDateTime 等 Java 8 时间 API
        mapper.registerModule(new JavaTimeModule());

        // 禁止将日期序列化为时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return mapper;
    }
}
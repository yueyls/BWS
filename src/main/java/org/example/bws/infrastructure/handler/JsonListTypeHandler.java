package org.example.bws.infrastructure.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.example.bws.domain.model.AlertRule;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(List.class)       // 映射Java类型
@MappedJdbcTypes(JdbcType.OTHER) // 映射JDBC类型（对应数据库JSON类型）
public class JsonListTypeHandler extends BaseTypeHandler<List<AlertRule>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<List<AlertRule>> TYPE_REFERENCE = 
        new TypeReference<List<AlertRule>>() {};

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<AlertRule> parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (Exception e) {
            throw new SQLException("JSON序列化失败: " + parameter, e);
        }
    }

    @Override
    public List<AlertRule> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public List<AlertRule> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public List<AlertRule> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private List<AlertRule> parseJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, TYPE_REFERENCE);
        } catch (Exception e) {
            throw new RuntimeException("JSON反序列化失败: " + json, e);
        }
    }
}
package org.example.bws.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.bws.domain.model.SignalReport;

import java.util.List;

@Mapper
public interface SignalReportRepository {
    // 批量插入（自动回填生成的id到对象中）
    int batchInsert(@Param("list") List<SignalReport> reports);

    // 根据处理状态查询
    List<SignalReport> selectByHandledStatus(@Param("handled") Boolean isHandled);

    // 根据ID标记为已处理
    int markAsHandled(@Param("id") Long id);
}
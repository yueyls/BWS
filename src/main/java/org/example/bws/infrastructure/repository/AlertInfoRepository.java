package org.example.bws.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.bws.domain.model.AlertInfo;

import java.util.List;

@Mapper
public interface AlertInfoRepository {
    // 批量插入（自动回填生成的id到对象中）
    int batchInsert(@Param("list") List<AlertInfo> alerts);

    // 根据car_id查询并按时间倒序排序
    List<AlertInfo> selectByCarIdWithSort(@Param("carId") int carId);
}
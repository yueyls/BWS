package org.example.bws.infrastructure.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.bws.domain.model.WarnInfo;

import java.util.List;


@Mapper
public interface WarnInfoRepository {
    int batchInsert(@Param("list") List<WarnInfo> warns);

    List<WarnInfo> selectByCarIdWithSort(@Param("carId") int carId);
}
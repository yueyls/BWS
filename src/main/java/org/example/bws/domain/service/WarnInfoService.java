package org.example.bws.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.model.WarnInfo;
import org.example.bws.infrastructure.repository.WarnInfoRepository;
import org.example.bws.shared.utils.DtoToModelConverter;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarnInfoService {

    @Autowired
    private WarnInfoRepository warnInfoRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RedissonClient redissonClient;

    private static final String WARN_INFO_CACHE_KEY_PREFIX = "warn_info:car_id:";

    public List<WarnInfoDto> findByCarId(int carID) throws JsonProcessingException {
        String cacheKey = WARN_INFO_CACHE_KEY_PREFIX + carID;

        RList<String> cachedWarnInfos = redissonClient.getList(cacheKey);
        if (!cachedWarnInfos.isEmpty()) {
            return cachedWarnInfos.stream()
                    .map(obj -> {
                        try {
                            return objectMapper.readValue(obj,WarnInfoDto.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(w -> w.getWarnLevel() != -1)
                    .collect(Collectors.toList());
        } else {
            List<WarnInfo> warnInfos = warnInfoRepository.selectByCarIdWithSort(carID);
            if (warnInfos.isEmpty()) {
                throw new RuntimeException("该车无预警信息");
            }

            List<WarnInfoDto> warnInfoDtos = warnInfos.stream()
                    .map(DtoToModelConverter::convertToDto)
                    .filter(w -> w.getWarnLevel() != -1)
                    .collect(Collectors.toList());

            List<String> warnInfoCache=new ArrayList<>();
            for (WarnInfoDto warnInfoDto : warnInfoDtos) {
                String s = objectMapper.writeValueAsString(warnInfoDto);
                System.out.println(s);
                warnInfoCache.add(s);
            }
            cachedWarnInfos.addAll(warnInfoCache);

            return warnInfoDtos;
        }
    }
}
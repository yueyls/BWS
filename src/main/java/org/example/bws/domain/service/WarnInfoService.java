package org.example.bws.domain.service;


import org.example.bws.domain.dto.WarnInfoDto;
import org.example.bws.domain.model.WarnInfo;
import org.example.bws.infrastructure.repository.WarnInfoRepository;
import org.example.bws.shared.utils.DtoToModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarnInfoService {

    @Autowired
    WarnInfoRepository warnInfoRepository;

    public List<WarnInfoDto>  findByCarId(int carID) {
        List<WarnInfo> warnInfos = warnInfoRepository.selectByCarIdWithSort(carID);
        if (warnInfos.isEmpty()) {
            throw new RuntimeException("该车无预警信息");
        }

        return warnInfos.stream()
                .map(w-> DtoToModelConverter.convertToDto(w))
                .collect(Collectors.toList());
    }
}

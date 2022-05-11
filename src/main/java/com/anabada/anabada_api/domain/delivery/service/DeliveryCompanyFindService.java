package com.anabada.anabada_api.domain.delivery.service;


import com.anabada.anabada_api.domain.delivery.entity.DeliveryCompanyVO;
import com.anabada.anabada_api.domain.delivery.repository.DeliveryCompanyRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DeliveryCompanyFindService {
    DeliveryCompanyRepository deliveryCompanyRepository;
    DeliveryFindService deliveryFindService;

    public DeliveryCompanyFindService(DeliveryCompanyRepository deliveryCompanyRepository, DeliveryFindService deliveryFindService) {
        this.deliveryCompanyRepository = deliveryCompanyRepository;
        this.deliveryFindService = deliveryFindService;
    }


    @Transactional(readOnly = true)
    public DeliveryCompanyVO findByIdx(Long idx){
        Optional<DeliveryCompanyVO> company = deliveryCompanyRepository.findById(idx);

        if (company.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return company.get();
    }


}

package com.anabada.anabada_api.service.delivery;


import com.anabada.anabada_api.domain.delivery.DeliveryCompanyVO;
import com.anabada.anabada_api.repository.DeliveryCompanyRepository;
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
    public DeliveryCompanyVO findByIdx(Long idx)throws NotFoundException{
        Optional<DeliveryCompanyVO>company=deliveryCompanyRepository.findById(idx);

        if(company.isEmpty())
            throw new NotFoundException("invalid company idx");

        return company.get();
    }



}

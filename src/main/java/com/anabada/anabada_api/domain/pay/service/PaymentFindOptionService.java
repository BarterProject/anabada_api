package com.anabada.anabada_api.domain.pay.service;

import com.anabada.anabada_api.domain.pay.entity.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.repository.PaymentOptionRepository;
import com.anabada.anabada_api.domain.pay.dto.PaymentOptionDTO;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentFindOptionService {

    PaymentOptionRepository paymentOptionRepository;

    public PaymentFindOptionService(PaymentOptionRepository paymentOptionRepository) {
        this.paymentOptionRepository = paymentOptionRepository;
    }

    @Transactional(readOnly = true)
    public PaymentOptionVO findByIdx(Long idx) {
        Optional<PaymentOptionVO> paymentOptions = paymentOptionRepository.findById(idx);

        if(paymentOptions.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return paymentOptions.get();

    }

    @Transactional(readOnly = true)
    public List<PaymentOptionVO> findAll() {
        return paymentOptionRepository.findAll();
    }


}

//    @Transactional(readOnly = true)
//    public PaymentOptionDTO findByIdxDTO(Long idx) throws NotFoundException {
//
//        PaymentOptionVO paymentOption = this.findByIdx(idx);
//
//        return paymentOption.dto();
//    }

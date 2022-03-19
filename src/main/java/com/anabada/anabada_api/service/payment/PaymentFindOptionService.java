package com.anabada.anabada_api.service.payment;

import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.dto.payment.PaymentOptionDTO;
import com.anabada.anabada_api.repository.PaymentOptionRepository;
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
    public PaymentOptionVO findByIdx(Long idx) throws NotFoundException {
        Optional<PaymentOptionVO> paymentOptions = paymentOptionRepository.findById(idx);

        if(paymentOptions.isEmpty())
            throw new NotFoundException("invalid payment option idx");

        return paymentOptions.get();

    }

    @Transactional(readOnly = true)
    public PaymentOptionDTO findByIdxDTO(Long idx) throws NotFoundException {

        PaymentOptionVO paymentOption = this.findByIdx(idx);

        return paymentOption.dto();
    }

    @Transactional(readOnly = true)
    public List<PaymentOptionDTO> findAll() throws NotFoundException {

        List<PaymentOptionVO> options = paymentOptionRepository.findAll();

        return options.stream().map(PaymentOptionVO::dto).collect(Collectors.toList());
    }


}

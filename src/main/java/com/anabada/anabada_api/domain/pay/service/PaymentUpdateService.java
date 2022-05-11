package com.anabada.anabada_api.domain.pay.service;

import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.pay.dto.CreatePayment;
import com.anabada.anabada_api.domain.pay.entity.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.entity.PaymentVO;

import com.anabada.anabada_api.domain.pay.dto.PaymentDTO;
import com.anabada.anabada_api.domain.pay.repository.PayRepository;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;


@Service

public class PaymentUpdateService {

    PayRepository payRepository;

    PaymentFindService paymentFindService;
    ItemFindService itemFindService;
    PaymentFindOptionService paymentFindOptionService;

    public PaymentUpdateService(PayRepository payRepository, PaymentFindService paymentFindService, ItemFindService itemFindService, PaymentFindOptionService paymentFindOptionService) {
        this.payRepository = payRepository;
        this.paymentFindService = paymentFindService;
        this.itemFindService = itemFindService;
        this.paymentFindOptionService = paymentFindOptionService;
    }

    @Transactional
    public PaymentVO save(CreatePayment.Request request) {
        PaymentOptionVO paymentOption = paymentFindOptionService.findByIdx(request.getOptionIdx());

        PaymentVO payment = PaymentVO.builder()
                .amount(request.getAmount())
                .state(PaymentVO.STATE.ACTIVATED.ordinal())
                .paymentOption(paymentOption)
                .build();

        return payRepository.save(payment);
    }
}

//    @Transactional
//    public PaymentVO update(Long idx, PaymentDTO paymentDTO) {
//        ItemVO item = itemFindService.findByIdx(idx);
//        PaymentVO payment = paymentFindService.findByIdx(idx);
//
//        if (payment.getItem() != item)
//            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);
//
//        payment.update(paymentDTO.getAmount(), paymentDTO.getState(), payment.getPaymentOption());
//        return payRepository.save(payment);
//    }


//    @Transactional
//    public void delete(Long idx) throws NotFoundException {
//        ItemVO item = itemFindService.findByIdx(idx);
//        PaymentVO payment = paymentFindService.findByIdx(idx);
//
//        if (payment.getItem() != item)
//            throw new NotFoundException("not your item");
//
//        payRepository.delete(payment);
//
//    }
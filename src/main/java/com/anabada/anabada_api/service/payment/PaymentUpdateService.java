package com.anabada.anabada_api.service.payment;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;

import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.repository.ItemRepository;
import com.anabada.anabada_api.repository.PayRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;


@Service
@RequiredArgsConstructor
public class PaymentUpdateService {

    PayRepository payRepository;

    PaymentFindService paymentFindService;

    ItemFindService itemFindService;

    ItemRepository itemRepository;
    PaymentOptionService paymentOptionService;

    public PaymentUpdateService(PayRepository payRepository, PaymentFindService paymentFindService, ItemFindService itemFindService, ItemRepository itemRepository, PaymentOptionService paymentOptionService) {
        this.payRepository = payRepository;
        this.paymentFindService = paymentFindService;
        this.itemFindService = itemFindService;
        this.itemRepository = itemRepository;
        this.paymentOptionService = paymentOptionService;
    }

    @Transactional
    public PaymentVO save(PaymentVO payment) {
        return payRepository.save(payment);
    }

    @Transactional
    public PaymentDTO save(Long idx, PaymentDTO paymentDTO) throws NotFoundException {
        // 엔티티 조회
        ItemVO item = itemFindService.findByIdx(idx);

        //결제 정보 생성
        PaymentVO payment = paymentDTO.toEntity();
        payment.setItem(item);

        //결제 정보 저장
        PaymentVO savedPayment = this.save(payment);

        return savedPayment.dto();
    }

    @Transactional
    public PaymentDTO update(Long idx, PaymentDTO paymentDTO) throws NotFoundException, AuthException {
        ItemVO item = itemFindService.findByIdx(idx);
        PaymentVO payment = paymentFindService.findByIdx(idx);

        if (payment.getItem() != item)
            throw new NotFoundException("not your item");

        payment.update(paymentDTO.getAmount(), paymentDTO.getState(), payment.getPaymentOption());
        this.save(payment);
        return payment.dto();
    }


    @Transactional
    public void delete(Long idx) throws NotFoundException {
        ItemVO item = itemFindService.findByIdx(idx);
        PaymentVO payment = paymentFindService.findByIdx(idx);

        if (payment.getItem() != item)
            throw new NotFoundException("not your item");

        payRepository.delete(payment);

    }
}

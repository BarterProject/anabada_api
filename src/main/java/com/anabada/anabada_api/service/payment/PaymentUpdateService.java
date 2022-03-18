package com.anabada.anabada_api.service.payment;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;

import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.repository.ItemRepository;
import com.anabada.anabada_api.repository.PayRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.item.ItemUpdateService;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;


@Service

public class PaymentUpdateService {

    PayRepository payRepository;

    PaymentFindService paymentFindService;
    ItemFindService itemFindService;
    ItemRepository itemRepository;
    PaymentOptionService paymentOptionService;
    PaymentFindOptionService paymentFindOptionService;
    ItemUpdateService itemUpdateService;

    public PaymentUpdateService(PayRepository payRepository, PaymentFindService paymentFindService, ItemFindService itemFindService, ItemRepository itemRepository, PaymentOptionService paymentOptionService, PaymentFindOptionService paymentFindOptionService, ItemUpdateService itemUpdateService) {
        this.payRepository = payRepository;
        this.paymentFindService = paymentFindService;
        this.itemFindService = itemFindService;
        this.itemRepository = itemRepository;
        this.paymentOptionService = paymentOptionService;
        this.paymentFindOptionService = paymentFindOptionService;
        this.itemUpdateService = itemUpdateService;
    }

    @Transactional
    public PaymentVO save(PaymentVO payment) {
        return payRepository.save(payment);
    }

    @Transactional
    public PaymentDTO save(Long idx, PaymentDTO paymentDTO) throws NotFoundException {

        ItemVO item = itemFindService.findByIdx(idx);

        if(item.getPayment() != null)
            throw new DuplicateRequestException("payment already registered"); //TODO 수정

        // 결제 옵션에서 결제 옵션 idx를 가져온다.
        PaymentOptionVO paymentOption = paymentFindOptionService.findByIdx(paymentDTO.getPaymentOption().getIdx());

        //결제 정보 저장시에는  결제옵션을 가져와 저장해야한다.
        PaymentVO payment = PaymentVO.builder()
                .amount(paymentDTO.getAmount())
                .state(1L)
                .paymentOption(paymentOption)
                .build();

        //아이템에 결제정보를 저장 -> 이후 아이템을 save하면 바로 save되게
        //이후 아이템에서도 결제 정보를 저장해야돼
        item.setPayment(payment);

        itemUpdateService.save(item);

        return item.getPayment().dto(true);

    }

    @Transactional
    public PaymentDTO update(Long idx, PaymentDTO paymentDTO) throws NotFoundException, AuthException {
        ItemVO item = itemFindService.findByIdx(idx);
        PaymentVO payment = paymentFindService.findByIdx(idx);

        if (payment.getItem() != item)
            throw new NotFoundException("not your item");

        payment.update(paymentDTO.getAmount(), paymentDTO.getState(), payment.getPaymentOption());
        this.save(payment);
        return payment.dto(true);
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

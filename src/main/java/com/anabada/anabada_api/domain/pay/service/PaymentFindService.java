package com.anabada.anabada_api.domain.pay.service;

import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.pay.entity.PaymentVO;
import com.anabada.anabada_api.domain.pay.dto.PagePaymentDTO;
import com.anabada.anabada_api.domain.pay.dto.PaymentDTO;
import com.anabada.anabada_api.domain.pay.repository.PayRepository;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentFindService {
    PayRepository payRepository;
    ItemFindService itemFindService;
    UserFindService userFindService;

    public PaymentFindService(PayRepository payRepository, ItemFindService itemFindService, UserFindService userFindService) {
        this.payRepository = payRepository;
        this.itemFindService = itemFindService;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public PaymentVO findByItemIdx(Long itemIdx){
        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if(item.getRegistrant() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        PaymentVO pay = item.getPayment();
        return pay;
    }

//    @Transactional(readOnly = true)
//    public PaymentVO findByIdx(Long idx) {
//        Optional<PaymentVO> payment = payRepository.findById(idx);
//
//        if (payment.isEmpty())
//            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);
//
//        return payment.get();
//    }

    /*@Transactional(readOnly = true)
    public PagePaymentDTO findAllWithAuth(Pageable pageable)throws AuthException{
        UserVO user=userFindService.getMyUserWithAuthorities();

        Page<PaymentVO> page=payRepository.findAllByUser(user,pageable);
        List<PaymentDTO>payments=page.stream().map(i->i.dto()).collect(Collectors.toList());

        return PagePaymentDTO.builder()
                .payments(payments)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages()-1)
                .build();

     */
}


//    @Transactional(readOnly = true)
//    public PagePaymentDTO findAll(Pageable pageable) {
//        Page<PaymentVO> page = payRepository.findAll(pageable);
//        List<PaymentDTO> payments = page.stream().map(i -> i.dto(false)).collect(Collectors.toList());
//
//        return PagePaymentDTO.builder()
//                .payments(payments)
//                .currentPage(pageable.getPageNumber())
//                .totalPage(page.getTotalPages() - 1)
//                .build();
//    }




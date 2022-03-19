package com.anabada.anabada_api.service.payment;

import com.anabada.anabada_api.domain.pay.PaymentVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.payment.PagePaymentDTO;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.repository.PayRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentFindService {
    @Autowired
    PayRepository payRepository;
    @Autowired
    ItemFindService itemFindService;
    UserFindService userFindService;

    public PaymentFindService(PayRepository payRepository,ItemFindService itemFindService,UserFindService userFindService)
    {
        this.payRepository=payRepository;
        this.itemFindService=itemFindService;
        this.userFindService=userFindService;

    }

    @Transactional(readOnly = true)
    public PaymentVO findByIdx(Long idx)throws NotFoundException{
        Optional<PaymentVO>payment=payRepository.findById(idx);

        if(payment.isEmpty())
            throw new NotFoundException("invalid");

        return payment.get();
    }

    @Transactional(readOnly = true)
    public PaymentDTO findByIdxDTO(Long idx) throws NotFoundException {

        PaymentVO payment = this.findByIdx(idx);

        return payment.dto(true);
    }

    @Transactional(readOnly = true)
    public PagePaymentDTO findAll(Pageable pageable){
        Page<PaymentVO> page=payRepository.findAll(pageable);
        List<PaymentDTO> payments=page.stream().map(i->i.dto(false)).collect(Collectors.toList());

        return PagePaymentDTO.builder()
                .payments(payments)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages()-1)
                .build();
    }

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





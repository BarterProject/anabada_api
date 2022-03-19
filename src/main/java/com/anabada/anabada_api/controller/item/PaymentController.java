package com.anabada.anabada_api.controller.item;


import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.payment.PagePaymentDTO;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.dto.payment.PaymentOptionDTO;
import com.anabada.anabada_api.service.payment.PaymentFindOptionService;
import com.anabada.anabada_api.service.payment.PaymentFindService;
import com.anabada.anabada_api.service.payment.PaymentUpdateService;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.io.IOException;
import java.util.List;

@Log
@RestController
@RequestMapping("/api")
public class PaymentController {

    PaymentFindService paymentFindService;
    PaymentUpdateService paymentUpdateService;
    PaymentFindOptionService paymentFindOptionService;


    public PaymentController(PaymentFindService paymentFindService, PaymentFindOptionService paymentFindOptionService, PaymentUpdateService paymentUpdateService) {
        this.paymentFindService = paymentFindService;
        this.paymentUpdateService = paymentUpdateService;
        this.paymentFindOptionService = paymentFindOptionService;
    }

    @GetMapping("/items/payments/{payment-idx}")// 단일 결제 조회
    public ResponseEntity<PaymentDTO> getPaymentByIdx(@PathVariable(value = "payment-idx") Long idx) throws NotFoundException {

        PaymentDTO dto = paymentFindService.findByIdxDTO(idx);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(path = "/items/payments/options")
    public ResponseEntity<List<PaymentOptionDTO>> getPaymentOptionByIdx() throws NotFoundException {
        List<PaymentOptionDTO> options = paymentFindOptionService.findAll();
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    /* @GetMapping("/user/payments") //나의 결제 내역 조회
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PagePaymentDTO> getAllMyPayments(@PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable) throws AuthException {
        PagePaymentDTO page = paymentFindService.findAllWithAuth(pageable);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }
    */
//    @PostMapping(path = "/items/{item-idx}/payments") // 결제 정보 저장
//    // @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<PaymentDTO> savePayment(
//            @PathVariable(value = "item-idx") Long idx,
//            @RequestBody @Validated(ValidationGroups.paymentGroup.class) PaymentDTO paymentDTO) throws NotFoundException {
//
//        PaymentDTO savePayment = paymentUpdateService.save(idx, paymentDTO);
//        return new ResponseEntity<>(savePayment, HttpStatus.CREATED);
//    }

    @PutMapping("/items/payments/{payment-idx}") //결제정보 수정
    //  @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PaymentDTO> modifyPayment(
            @PathVariable(value = "payment-idx") Long idx, PaymentDTO paymentDTO
    ) throws NotFoundException, AuthException {
        PaymentDTO updatedPayment = paymentUpdateService.update(idx, paymentDTO);
        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }

    @DeleteMapping("/items/payments/{payment-idx}") //결제 정보 삭제
    //@PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageDTO> deletePayment(
            @PathVariable(value = "payment-idx") Long idx) throws NotFoundException {
        paymentUpdateService.delete(idx);
        return new ResponseEntity<>(new MessageDTO("payment deleted"), HttpStatus.NO_CONTENT);
    }


}

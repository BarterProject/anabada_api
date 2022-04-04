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

    /**
     * @param idx : 결제 idx
     * @return PaymentDTO : 결제 정보
     * @throws NotFoundException : 존재하지 않은 payment idx
     */
    @GetMapping("/items/payments/{payment-idx}")// 단일 결제 조회
    public ResponseEntity<PaymentDTO> getPaymentByIdx(@PathVariable(value = "payment-idx") Long idx) throws NotFoundException {

        PaymentDTO dto = paymentFindService.findByIdxDTO(idx);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * @return List> PaymentOptionDTO : 결제 옵션 리스트
     * @throws NotFoundException 옵션 리스트가 존재하지 않는 경우
     */
    @GetMapping(path = "/items/payments/options")
    public ResponseEntity<List<PaymentOptionDTO>> getPaymentOptionByIdx() throws NotFoundException {
        List<PaymentOptionDTO> options = paymentFindOptionService.findAll();
        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    /**
     * @param idx        수정할 결제 idx
     * @param paymentDTO 결제 정보
     * @return PaymentDTO  수정된 결제 정보
     * amount : 금액
     * state : 결제 상태
     * paymentOption:  결제 옵션
     * @throws NotFoundException 존재하지 않는 결제 idx
     * @throws AuthException     올바르지 않는 권한 오류
     *                           유효하지 않은 토큰
     */
    @PutMapping("/items/payments/{payment-idx}") //결제정보 수정
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PaymentDTO> modifyPayment(
            @PathVariable(value = "payment-idx") Long idx, PaymentDTO paymentDTO
    ) throws NotFoundException, AuthException {
        PaymentDTO updatedPayment = paymentUpdateService.update(idx, paymentDTO);
        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }

    /**
     * @param idx 삭제할 결제 idx
     * @return  message
     * @throws NotFoundException 존재하지않은 결제 idx
     */
    @DeleteMapping("/items/payments/{payment-idx}") //결제 정보 삭제
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageDTO> deletePayment(
            @PathVariable(value = "payment-idx") Long idx) throws NotFoundException {
        paymentUpdateService.delete(idx);
        return new ResponseEntity<>(new MessageDTO("payment deleted"), HttpStatus.NO_CONTENT);
    }


}

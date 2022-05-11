package com.anabada.anabada_api.domain.pay.controller;

import com.anabada.anabada_api.domain.pay.dto.PaymentOptionDTO;
import com.anabada.anabada_api.domain.pay.entity.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.service.PaymentFindOptionService;
import com.anabada.anabada_api.domain.pay.service.PaymentFindService;
import com.anabada.anabada_api.domain.pay.service.PaymentUpdateService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping(path = "/items/payments/options")
    public ResponseEntity<List<PaymentOptionDTO>> getPaymentOptionByIdx() {
        List<PaymentOptionVO> options = paymentFindOptionService.findAll();
        List<PaymentOptionDTO> dtos = options.stream().map(PaymentOptionDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}

//    @GetMapping("/items/payments/{payment-idx}")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
//    public ResponseEntity<PaymentDTO> getPaymentByIdx(@PathVariable(value = "payment-idx") Long idx) {
//
//        PaymentVO vo = paymentFindService.findByIdx(idx);
//        PaymentDTO dto = PaymentDTO.fromEntity(vo);
//
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }

//    @PutMapping("/items/payments/{payment-idx}") //결제정보 수정
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<PaymentDTO> modifyPayment(
//            @PathVariable(value = "payment-idx") Long idx, PaymentDTO paymentDTO
//    ) throws {
//        PaymentDTO updatedPayment = paymentUpdateService.update(idx, paymentDTO);
//        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
//    }

//    @DeleteMapping("/items/payments/{payment-idx}") //결제 정보 삭제
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public ResponseEntity<MessageDTO> deletePayment(
//            @PathVariable(value = "payment-idx") Long idx) {
//        paymentUpdateService.delete(idx);
//        return new ResponseEntity<>(new MessageDTO("payment deleted"), HttpStatus.NO_CONTENT);
//    }

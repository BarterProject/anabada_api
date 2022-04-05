package com.anabada.anabada_api.controller;



import com.anabada.anabada_api.domain.DeliveryVO;
import com.anabada.anabada_api.dto.DeliveryDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.service.delivery.DeliveryRequestService;

import javassist.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api")
public class DeliveryController {


    public DeliveryController(DeliveryRequestService deliveryRequestService) {
        this.deliveryRequestService = deliveryRequestService;
    }

    DeliveryRequestService deliveryRequestService;

    /**
     * 배송요청 정보 저장
     * 
     * @param idx : 배송 요청 대상 아이템 idx
     * @param deliveryDTO 배송 요청 정보
     *                    phone : 전화번호
     *                    receiverName:수령자 이름
     *                    clauseAgree : 약관 동의 여부
     * @return DeliveryDTO : 저장된 배송 요청
     * @throws NotFoundException 유효하지 않는 item idx
     * @throws AuthException 유효하지 않는 토큰
     */
    @PostMapping("/user/items/{item-idx}/deliveries")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<DeliveryDTO>saveDelivery(
            @PathVariable (value = "item-idx")Long idx,
            @RequestBody @Validated(ValidationGroups.deliveryRequestGroup.class)DeliveryDTO deliveryDTO)
        throws NotFoundException, AuthException{
         DeliveryDTO saveDelivery=deliveryRequestService.save(idx,deliveryDTO);
        return new ResponseEntity<>(saveDelivery, HttpStatus.CREATED);
    }
}

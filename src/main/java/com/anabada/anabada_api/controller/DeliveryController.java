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

package com.anabada.anabada_api.domain.delivery.controller;


import com.anabada.anabada_api.domain.delivery.dto.CreateDelivery;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryTrackingDTO;
import com.anabada.anabada_api.domain.delivery.dto.RegisterTracking;
import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.delivery.service.DeliveryCompanyFindService;
import com.anabada.anabada_api.domain.delivery.service.DeliveryFindService;
import com.anabada.anabada_api.domain.delivery.service.DeliveryUpdateService;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryDTO;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;

@RestController
@RequestMapping("/api")
public class DeliveryController {

    DeliveryUpdateService deliveryUpdateService;
    DeliveryFindService deliveryFindService;
    ItemFindService itemFindService;
    DeliveryCompanyFindService deliveryCompanyFindService;

    public DeliveryController(DeliveryUpdateService deliveryUpdateService, DeliveryFindService deliveryFindService, ItemFindService itemFindService, DeliveryCompanyFindService deliveryCompanyFindService) {
        this.deliveryUpdateService = deliveryUpdateService;
        this.deliveryFindService = deliveryFindService;
        this.itemFindService = itemFindService;
        this.deliveryCompanyFindService = deliveryCompanyFindService;
    }


    @PostMapping("/user/items/{item-idx}/deliveries")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<DeliveryDTO> saveDelivery(
            @PathVariable(value = "item-idx") Long idx,
            @RequestBody CreateDelivery.Request request) {

        Long id = deliveryUpdateService.save(idx, request);
        DeliveryVO vo = deliveryFindService.findByIdx(id);
        DeliveryDTO dto = DeliveryDTO.fromEntity(vo);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/user/items/deliveries/{delivery-idx}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<DeliveryDTO> saveTracking(
            @PathVariable(value = "delivery-idx") Long idx,
            @RequestBody RegisterTracking.Request request
    ) {

        DeliveryVO vo = deliveryUpdateService.saveTrackingNumber(idx, request);
        DeliveryDTO dto = DeliveryDTO.fromEntity(vo);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("user/items/{item-idx}/deliveries")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<DeliveryDTO> getDelivery(
            @PathVariable(value = "item-idx") Long itemIdx
    ) {
        DeliveryVO vo = deliveryFindService.findByItem(itemIdx);
        DeliveryDTO dto = DeliveryDTO.fromEntity(vo);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("user/items/{item-idx}/deliveries/details")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLS_AMDIN')")
    public ResponseEntity<DeliveryTrackingDTO> getDeliveryTracking(
            @PathVariable(value = "item-idx") Long itemIdx
    ) {
        DeliveryTrackingDTO tracking = deliveryFindService.getTracking(itemIdx);

        return new ResponseEntity<>(tracking, HttpStatus.OK);
    }
}

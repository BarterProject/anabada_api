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


    @PostMapping("/v2/user/items/{item-idx}/deliveries")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<CreateDelivery.Response> saveDelivery(
            @PathVariable(value = "item-idx") Long idx,
            @RequestBody CreateDelivery.Request request) {

        Long id = deliveryUpdateService.save(idx, request);

        return new ResponseEntity<>(new CreateDelivery.Response(id), HttpStatus.CREATED);
    }

    @PostMapping("/v2/user/items/deliveries/{delivery-idx}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<RegisterTracking.Response> saveTracking(
            @PathVariable(value = "delivery-idx") Long idx,
            @RequestBody RegisterTracking.Request request
    ) {
        deliveryUpdateService.saveTrackingNumber(idx, request);

        return new ResponseEntity<>(new RegisterTracking.Response("saved"), HttpStatus.OK);
    }

    @GetMapping("/v2/user/items/{item-idx}/deliveries")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<DeliveryDTO> getDelivery(
            @PathVariable(value = "item-idx") Long itemIdx
    ) {
        DeliveryVO vo = deliveryFindService.findByItem(itemIdx);
        DeliveryDTO dto = DeliveryDTO.fromEntity(vo);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/v2/user/items/{item-idx}/deliveries/tracking")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<DeliveryTrackingDTO> getDeliveryTracking(
            @PathVariable(value = "item-idx") Long itemIdx
    ) {
        DeliveryTrackingDTO tracking = deliveryFindService.getTracking(itemIdx);

        return new ResponseEntity<>(tracking, HttpStatus.OK);
    }
}

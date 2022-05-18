package com.anabada.anabada_api.domain.delivery.controller;


import com.anabada.anabada_api.domain.delivery.dto.CreateDelivery;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryDTO;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryTrackingDTO;
import com.anabada.anabada_api.domain.delivery.dto.RegisterTracking;
import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.delivery.service.DeliveryCompanyFindService;
import com.anabada.anabada_api.domain.delivery.service.DeliveryFindService;
import com.anabada.anabada_api.domain.delivery.service.DeliveryUpdateService;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.firebase.FCMService;
import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
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
    FCMService fcmService;

    public DeliveryController(DeliveryUpdateService deliveryUpdateService, DeliveryFindService deliveryFindService, ItemFindService itemFindService, DeliveryCompanyFindService deliveryCompanyFindService, FCMService fcmService) {
        this.deliveryUpdateService = deliveryUpdateService;
        this.deliveryFindService = deliveryFindService;
        this.itemFindService = itemFindService;
        this.deliveryCompanyFindService = deliveryCompanyFindService;
        this.fcmService = fcmService;
    }


    @PostMapping("/v2/user/items/{item-idx}/deliveries")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<CreateDelivery.Response> saveDelivery(
            @PathVariable(value = "item-idx") Long idx,
            @RequestBody CreateDelivery.Request request) {

        Long id = deliveryUpdateService.save(idx, request);
        fcmService.sendDeliveryRequestedNotice(idx);

        return new ResponseEntity<>(new CreateDelivery.Response(id), HttpStatus.CREATED);
    }

    @PostMapping("/v2/user/items/deliveries/{delivery-idx}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<RegisterTracking.Response> saveTracking(
            @PathVariable(value = "delivery-idx") Long idx,
            @RequestBody RegisterTracking.Request request
    ) {
        deliveryUpdateService.saveTrackingNumber(idx, request);

        fcmService.sendDeliveryStartedNotice(idx);

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
        String trackingNumber = deliveryFindService.findByItem(itemIdx).getTrackingNumber();

        if (trackingNumber == null)
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        DeliveryTrackingDTO tracking = deliveryFindService.getTracking(trackingNumber);

        return new ResponseEntity<>(tracking, HttpStatus.OK);
    }

    /* 관리자 기능 */
    @PostMapping(value = "/v2/admin/deliveries/{item-idx}/returnDeposit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> returnComplete(
            @PathVariable(value = "item-idx") Long itemIdx) {
        deliveryUpdateService.returnComplete(itemIdx);

        fcmService.sendReturnCompleteNotice(itemIdx);
        return new ResponseEntity<>(new MessageDTO("return deposit complete"), HttpStatus.OK);
    }
}

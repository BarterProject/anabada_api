package com.anabada.anabada_api.domain.delivery.service;

import com.anabada.anabada_api.domain.delivery.dto.CreateDelivery;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryTrackingDTO;
import com.anabada.anabada_api.domain.delivery.dto.RegisterTracking;
import com.anabada.anabada_api.domain.delivery.entity.DeliveryCompanyVO;
import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.delivery.repository.DeliveryRepository;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.item.service.ItemUpdateService;
import com.anabada.anabada_api.domain.message.service.RoomUpdateService;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DeliveryUpdateService {

    DeliveryRepository deliveryRepository;

    ItemFindService itemFindService;
    ItemUpdateService itemUpdateService;
    RoomUpdateService roomUpdateService;
    DeliveryFindService deliveryFindService;
    DeliveryCompanyFindService deliveryCompanyFindService;
    UserFindService userFindService;

    public DeliveryUpdateService(DeliveryRepository deliveryRepository, ItemFindService itemFindService, ItemUpdateService itemUpdateService, RoomUpdateService roomUpdateService, DeliveryFindService deliveryFindService, DeliveryCompanyFindService deliveryCompanyFindService, UserFindService userFindService) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.itemUpdateService = itemUpdateService;
        this.roomUpdateService = roomUpdateService;
        this.deliveryFindService = deliveryFindService;
        this.deliveryCompanyFindService = deliveryCompanyFindService;
        this.userFindService = userFindService;
    }

    @Transactional
    public DeliveryVO save(DeliveryVO vo) {
        return deliveryRepository.save(vo);
    }

    @Transactional
    public Long save(Long idx, CreateDelivery.Request request) {

        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(idx);

        if (item.getDelivery() != null)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        if (item.getOwner() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        DeliveryVO deliveryVO = DeliveryVO.builder()
                .receiverName(request.getReceiverName())
                .state(DeliveryVO.STATE.APPLIED.ordinal())
                .clauseAgree(request.isClauseAgree())
                .phone(request.getPhone())
                .address(request.getAddress())
                .dueAt(LocalDateTime.now().plusWeeks(2))
                .item(item)
                .build();

        item.setDelivery(deliveryVO);
        roomUpdateService.save(deliveryVO);

        item.closeRequest();

        return deliveryVO.getIdx();
    }

    @Transactional
    public Long saveTrackingNumber(Long deliveryIdx, RegisterTracking.Request request) {

        UserVO user = userFindService.getMyUserWithAuthorities();
        DeliveryVO delivery = deliveryFindService.findByIdx(deliveryIdx);

        if (delivery.getTrackingNumber() != null)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        if (delivery.getItem().getRegistrant() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        DeliveryCompanyVO company = deliveryCompanyFindService.findByIdx(request.getDeliveryCompanyIdx());
        delivery.setTrackingInfo(request.getTrackingNumber(), company);

        DeliveryTrackingDTO trackingInfo = deliveryFindService.getTracking(delivery.getTrackingNumber());

        if (!delivery.getItem().getName().equals(trackingInfo.getItemName()))
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        delivery.getItem().requestDeposit();

        return delivery.getIdx();
    }

    @Transactional
    public void returnComplete(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if (item.getState() != ItemVO.STATE.RETURN.ordinal())
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        item.completeItem();
    }

}

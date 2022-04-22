package com.anabada.anabada_api.service.delivery;

import com.anabada.anabada_api.domain.delivery.DeliveryCompanyVO;
import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.delivery.DeliveryDTO;
import com.anabada.anabada_api.dto.delivery.RegisterTrackingDTO;
import com.anabada.anabada_api.repository.DeliveryRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.item.ItemUpdateService;
import com.anabada.anabada_api.service.room.RoomUpdateService;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;

@Service
public class DeliveryUpdateService {

    DeliveryRepository deliveryRepository;

    ItemFindService itemFindService;
    ItemUpdateService itemUpdateService;
    RoomUpdateService roomUpdateService;
    DeliveryFindService deliveryFindService;
    DeliveryCompanyFindService deliveryCompanyFindService;

    public DeliveryUpdateService(DeliveryRepository deliveryRepository, ItemFindService itemFindService, ItemUpdateService itemUpdateService, RoomUpdateService roomUpdateService, DeliveryFindService deliveryFindService, DeliveryCompanyFindService deliveryCompanyFindService) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.itemUpdateService = itemUpdateService;
        this.roomUpdateService = roomUpdateService;
        this.deliveryFindService = deliveryFindService;
        this.deliveryCompanyFindService = deliveryCompanyFindService;
    }


    @Transactional
    public DeliveryVO save(DeliveryVO vo) {
        return deliveryRepository.save(vo);
    }


    @Transactional
    public DeliveryDTO save(Long idx, DeliveryDTO dto) throws AuthException, NotFoundException {

        ItemVO item = itemFindService.findByIdx(idx);

        if (item.getDelivery() != null)
            throw new DuplicateRequestException("이미 존재하는 배송요청입니다.");

        DeliveryVO deliveryVO = DeliveryVO.builder()
                .receiverName(dto.getReceiverName())
                .state(1L)
                .clauseAgree(dto.isClauseAgree())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .dueAt(LocalDateTime.now().plusMonths(1))
                .item(item)
                .build();


        item.setDelivery(deliveryVO);
        roomUpdateService.save(deliveryVO);

        return deliveryVO.dto(true, false, false);
    }

    @Transactional
    public DeliveryDTO saveTrackingNumber(Long itemIdx, RegisterTrackingDTO dto) throws NotFoundException {

        ItemVO item = itemFindService.findByIdx(itemIdx);

        DeliveryVO delivery = item.getDelivery();

        if (delivery == null)
            throw new NotFoundException("배송 요청되지 않은 아이템입니다.");

        if (delivery.getTrackingNumber() != null)
            throw new NotFoundException("이미 운송장이 등록되어있습니다");

        DeliveryCompanyVO company = deliveryCompanyFindService.findByIdx(dto.getDeliveryCompanyIdx());

        delivery.setTrackingInfo(dto.getTrackingNumber(), company);

        return this.save(delivery).dto(false, true, true);
    }


}

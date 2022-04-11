package com.anabada.anabada_api.service.delivery;

import com.anabada.anabada_api.domain.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.delivery.DeliveryDTO;
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
public class DeliveryRequestService {

    DeliveryRepository deliveryRepository;

    ItemFindService itemFindService;
    ItemUpdateService itemUpdateService;
    RoomUpdateService roomUpdateService;

    public DeliveryRequestService(DeliveryRepository deliveryRepository, ItemFindService itemFindService, ItemUpdateService itemUpdateService, RoomUpdateService roomUpdateService) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.itemUpdateService = itemUpdateService;
        this.roomUpdateService = roomUpdateService;
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

        return deliveryVO.dto(true);

    }
}

package com.anabada.anabada_api.service.delivery;

import com.anabada.anabada_api.domain.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.DeliveryDTO;
import com.anabada.anabada_api.repository.DeliveryRepository;
import com.anabada.anabada_api.repository.RoomRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.item.ItemUpdateService;
import com.anabada.anabada_api.service.user.UserFindService;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DeliveryRequestService {

    DeliveryRepository deliveryRepository;

    ItemFindService itemFindService;

    UserFindService userFindService;

    RoomRepository roomRepository;
    ItemUpdateService itemUpdateService;

    public DeliveryRequestService(DeliveryRepository deliveryRepository, ItemFindService itemFindService, UserFindService userFindService, RoomRepository roomRepository, ItemUpdateService itemUpdateService) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.userFindService = userFindService;
        this.roomRepository = roomRepository;
        this.itemUpdateService = itemUpdateService;
    }


    @Transactional
    public DeliveryVO save(DeliveryVO vo) {
        return deliveryRepository.save(vo);
    }

    @Transactional
    public RoomVO save(RoomVO vo) {
        return roomRepository.save(vo);
    }


    @Transactional
    public DeliveryDTO save(Long idx, DeliveryDTO dto) throws AuthException, NotFoundException {
        {
            ItemVO item = itemFindService.findByIdx(idx);
            UserVO user = userFindService.getMyUserWithAuthorities();
            UUID saveName = UUID.randomUUID();
            String receiver = dto.getReceiverName();


         if (item.getDelivery() != null) {
              throw new DuplicateRequestException("이미 존재하는 배송요청입니다.");
           }

            DeliveryVO deliveryVO = DeliveryVO.builder()
                    .receiverName(dto.getReceiverName())
                    .state(1L)
                    .clauseAgree(dto.isClauseAgree())
                    .phone(dto.getPhone())
                    .address(dto.getAddress())
                    .dueAt(LocalDateTime.now().plusMonths(1))
                    .item(item)
                    .build();

           if(item.getDelivery().getRoom()!=null) {
              throw new DuplicateRequestException("이미 존재하는 채팅방입니다.");
          }

            RoomVO roomVO = RoomVO.builder()
                    .name(saveName.toString())
                    .sender(user.getAuth().getName())
                    .receiver(receiver)
                    .state(1)
                    .delivery(deliveryVO)
                    .build();


            RoomVO savedRoom = roomRepository.save(roomVO);

            item.setDelivery(deliveryVO);
            deliveryVO = itemUpdateService.save(item).getDelivery();

            return deliveryVO.dto(true);
        }


    }


}

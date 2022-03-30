package com.anabada.anabada_api.service.delivery;

import com.anabada.anabada_api.domain.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.DeliveryDTO;
import com.anabada.anabada_api.repository.DeliveryRepository;
import com.anabada.anabada_api.repository.RoomRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
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

    public DeliveryRequestService(DeliveryRepository deliveryRepository, ItemFindService itemFindService, UserFindService userFindService, RoomRepository roomRepository) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.userFindService = userFindService;
        this.roomRepository = roomRepository;
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

            if (deliveryRepository.existsByState(dto.getState())) {
                throw new DuplicateRequestException("이미 배송요청한 아이템입니다");
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


             DeliveryVO savedDelivery=deliveryRepository.save(deliveryVO);
            createRoom(deliveryVO);
            return savedDelivery.dto(true);
        }


    }

    public RoomVO createRoom(DeliveryVO dto) throws AuthException{
        UserVO user = userFindService.getMyUserWithAuthorities();
        UUID saveName = UUID.randomUUID();
        String receiver = dto.getReceiverName();

       if (roomRepository.existsByReceiver(dto.getReceiverName())) {
          throw new DuplicateRequestException("이미 존재하는 채팅방입니다.");
      }

        RoomVO roomVO = RoomVO.builder()
                .name(saveName.toString())
                .sender(user.getAuth().getName())
                .receiver(receiver)
                .state(1)
                .build();

        RoomVO savedRoom = roomRepository.save(roomVO);
        return this.save(savedRoom);

    }


}

package com.anabada.anabada_api.domain.message.service;


import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.message.dto.RoomDTO;
import com.anabada.anabada_api.domain.message.repository.RoomRepository;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.UUID;

@Service
public class RoomUpdateService {

    RoomRepository roomRepository;

    UserFindService userFindService;

    RoomUserUpdateService roomUserUpdateService;

    public RoomUpdateService(RoomRepository roomRepository, UserFindService userFindService, RoomUserUpdateService roomUserUpdateService) {
        this.roomRepository = roomRepository;
        this.userFindService = userFindService;
        this.roomUserUpdateService = roomUserUpdateService;
    }


    @Transactional
    public RoomVO save(RoomVO room) {
        return roomRepository.save(room);
    }


    // 채팅방 생성
    @Transactional
    public void save(DeliveryVO deliveryVO){
        UUID saveName = UUID.randomUUID();
        RoomVO roomVO = RoomVO.builder()
                .name(saveName.toString())
                .state(1)
                .delivery(deliveryVO)
                .build();

        roomVO = this.save(roomVO);
        UserVO user = deliveryVO.getItem().getRegistrant(); //user부분 수정 필요
        roomUserUpdateService.save(user, roomVO);
        UserVO user2 = userFindService.getMyUserWithAuthorities();
        roomUserUpdateService.save(user2, roomVO);
    }


}

package com.anabada.anabada_api.service.room;


import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.room.RoomDTO;
import com.anabada.anabada_api.repository.RoomRepository;
import com.anabada.anabada_api.service.user.UserFindService;
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
    public RoomDTO save(DeliveryVO deliveryVO) throws NotFoundException, AuthException {
        UUID saveName = UUID.randomUUID();
        RoomVO roomVO = RoomVO.builder()
                .name(saveName.toString())
                .state(1)
                .delivery(deliveryVO)
                .build();

        roomVO = this.save(roomVO);
        UserVO user = deliveryVO.getItem().getOwner(); //user부분 수정 필요
        roomUserUpdateService.save(user, roomVO);
        UserVO user2 = userFindService.getMyUserWithAuthorities();
        roomUserUpdateService.save(user2, roomVO);

        return roomVO.dto(true);
    }


}

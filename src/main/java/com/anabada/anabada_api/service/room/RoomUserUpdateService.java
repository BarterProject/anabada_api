package com.anabada.anabada_api.service.room;

import com.anabada.anabada_api.domain.message.RoomUserVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.room.RoomUserDTO;
import com.anabada.anabada_api.repository.RoomUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomUserUpdateService {
    RoomUserRepository roomUserRepository;

    public RoomUserUpdateService(RoomUserRepository roomUserRepository) {
        this.roomUserRepository = roomUserRepository;
    }

    @Transactional
    public RoomUserVO save(RoomUserVO roomUserVO) {
        return roomUserRepository.save(roomUserVO);
    }

    @Transactional
    public RoomUserDTO save(UserVO user, RoomVO room) {
        RoomUserVO roomUserVO = RoomUserVO.builder()
                .user(user)
                .room(room)
                .build();

        roomUserVO = roomUserRepository.save(roomUserVO);

        return roomUserVO.dto(true, true);
    }

}

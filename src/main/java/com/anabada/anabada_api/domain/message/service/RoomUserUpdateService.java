package com.anabada.anabada_api.domain.message.service;

import com.anabada.anabada_api.domain.message.entity.RoomUserVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.message.dto.RoomUserDTO;
import com.anabada.anabada_api.domain.message.repository.RoomUserRepository;
import com.anabada.anabada_api.domain.user.entity.UserVO;
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
    public Long save(UserVO user, RoomVO room) {
        RoomUserVO roomUserVO = RoomUserVO.builder()
                .user(user)
                .room(room)
                .build();

        return roomUserRepository.save(roomUserVO).getIdx();
    }

}

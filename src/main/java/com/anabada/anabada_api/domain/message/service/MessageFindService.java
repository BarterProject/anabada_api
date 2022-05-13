package com.anabada.anabada_api.domain.message.service;

import com.anabada.anabada_api.domain.message.repository.MessageRepository;
import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.message.entity.RoomUserVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;

import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

@Service
public class MessageFindService {


    MessageRepository messageRepository;
    RoomFindService roomFindService;
    UserFindService userFindService;

    public MessageFindService(MessageRepository messageRepository, RoomFindService roomFindService, UserFindService userFindService) {
        this.messageRepository = messageRepository;
        this.roomFindService = roomFindService;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public Page<MessageVO> getMessagesByRoomIdx(long roomIdx, Pageable pageable){
        RoomVO room = roomFindService.getByIdx(roomIdx);
        UserVO user = userFindService.getMyUserWithAuthorities();

        boolean isOwner = false;

        for (RoomUserVO mapping : room.getMappings()) {
            if (user == mapping.getUser()) {
                isOwner = true;
                break;
            }
        }

        if(!isOwner) throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        return messageRepository.findByRoom(room, pageable);
    }

}








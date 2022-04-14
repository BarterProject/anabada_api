package com.anabada.anabada_api.service.message;

import com.anabada.anabada_api.domain.message.MessageVO;
import com.anabada.anabada_api.domain.message.RoomUserVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.MessageEntityDTO;
import com.anabada.anabada_api.repository.MessageRepository;
import com.anabada.anabada_api.service.room.RoomFindService;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;

@Service
public class MessageUpdateService {

    MessageRepository messageRepository;

    UserFindService userFindService;
    RoomFindService roomFindService;

    public MessageUpdateService(MessageRepository messageRepository, UserFindService userFindService, RoomFindService roomFindService) {
        this.messageRepository = messageRepository;
        this.userFindService = userFindService;
        this.roomFindService = roomFindService;
    }

    public MessageVO save(MessageVO vo){
        return messageRepository.save(vo);
    }

    @Transactional
    public MessageEntityDTO save(MessageEntityDTO dto) throws NotFoundException, AuthException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        RoomVO room = roomFindService.getByName(dto.getRoom().getName());

        List<RoomUserVO> roomUserMapping = room.getMappings();

        boolean isMyRoom = false;
        for(RoomUserVO em : roomUserMapping) {
            if (em.getUser() == user) {
                isMyRoom = true;
                break;
            }
        }

        if(!isMyRoom)
            throw new AuthException("not my own message room");

        MessageVO vo = MessageVO.builder()
                .sender(user)
                .room(room)
                .content(dto.getContent())
                .state(1)
                .build();

        return this.save(vo).dto();
    }


}








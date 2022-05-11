package com.anabada.anabada_api.domain.message.service;

import com.anabada.anabada_api.domain.message.dto.CreateMessage;
import com.anabada.anabada_api.domain.message.dto.MessageEntityDTO;
import com.anabada.anabada_api.domain.message.repository.MessageRepository;
import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.message.entity.RoomUserVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
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

    @Transactional
    public MessageVO save(CreateMessage.Request request){

        UserVO user = userFindService.getMyUserWithAuthorities();
        RoomVO room = roomFindService.getByName(request.getRoomName());

        List<RoomUserVO> roomUserMapping = room.getMappings();

        boolean isMyRoom = false;
        for(RoomUserVO em : roomUserMapping) {
            if (em.getUser() == user) {
                isMyRoom = true;
                break;
            }
        }

        if(!isMyRoom)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        MessageVO vo = MessageVO.builder()
                .sender(user)
                .room(room)
                .content(request.getContent())
                .state(1)
                .build();

        return messageRepository.save(vo);
    }


}








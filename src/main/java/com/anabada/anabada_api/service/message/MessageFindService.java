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
import java.util.stream.Collectors;

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
    public List<MessageEntityDTO> getMessagesByRoomIdx(long roomIdx) throws NotFoundException, AuthException {
        RoomVO room = roomFindService.getByIdx(roomIdx);
        UserVO user = userFindService.getMyUserWithAuthorities();

        boolean isOwner = false;

        for (RoomUserVO mapping : room.getMappings()) {
            if (user == mapping.getUser()) {
                isOwner = true;
                break;
            }
        }

        if(!isOwner) throw new AuthException("not your own room");

        List<MessageVO> messages = messageRepository.findByRoom(room);
        return messages.stream().map(MessageVO::dto).collect(Collectors.toList());
    }

}








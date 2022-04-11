package com.anabada.anabada_api.controller;



import com.anabada.anabada_api.dto.MessageEntityDTO;
import com.anabada.anabada_api.dto.room.RoomDTO;
import com.anabada.anabada_api.service.message.MessageFindService;

import com.anabada.anabada_api.service.room.RoomFindService;
import javassist.NotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {


    RoomFindService roomFindService;
    MessageFindService messageFindService;

    public MessageController(RoomFindService roomFindService, MessageFindService messageFindService) {
        this.roomFindService = roomFindService;
        this.messageFindService = messageFindService;
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<RoomDTO> getMyRooms() throws AuthException {
        return roomFindService.getMyRooms();
    }

    @GetMapping("/rooms/{room-idx}/messages")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<MessageEntityDTO> getMessagesByRoomIdx(
            @PathVariable("room-idx") Long roomIdx
    ) throws NotFoundException, AuthException {

        return messageFindService.getMessagesByRoomIdx(roomIdx);
    }


}

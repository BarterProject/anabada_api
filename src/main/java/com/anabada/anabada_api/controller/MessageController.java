package com.anabada.anabada_api.controller;


import com.anabada.anabada_api.domain.message.MessageVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.MessageEntityDTO;
import com.anabada.anabada_api.dto.room.RoomDTO;
import com.anabada.anabada_api.service.message.MessageFindService;

import com.anabada.anabada_api.service.message.MessageUpdateService;
import com.anabada.anabada_api.service.room.RoomFindService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {


    RoomFindService roomFindService;
    MessageFindService messageFindService;
    MessageUpdateService messageUpdateService;

    public MessageController(RoomFindService roomFindService, MessageFindService messageFindService, MessageUpdateService messageUpdateService) {
        this.roomFindService = roomFindService;
        this.messageFindService = messageFindService;
        this.messageUpdateService = messageUpdateService;
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<RoomDTO>> getMyRooms() throws AuthException {
        List<RoomDTO> myRooms = roomFindService.getMyRooms();

        return new ResponseEntity<>(myRooms, HttpStatus.OK);
    }

    @GetMapping("/rooms/{room-idx}/messages")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<MessageEntityDTO>> getMessagesByRoomIdx(
            @PathVariable("room-idx") Long roomIdx
    ) throws NotFoundException, AuthException {

        return new ResponseEntity<>(messageFindService.getMessagesByRoomIdx(roomIdx), HttpStatus.OK);
    }

    @PostMapping("/rooms/messages")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<MessageEntityDTO> saveMessage(
            @RequestBody MessageEntityDTO messageEntityDTO
    ) throws NotFoundException, AuthException {

        MessageEntityDTO message = messageUpdateService.save(messageEntityDTO);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


}

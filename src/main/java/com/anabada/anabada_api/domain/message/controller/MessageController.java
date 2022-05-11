package com.anabada.anabada_api.domain.message.controller;


import com.anabada.anabada_api.domain.message.dto.CreateMessage;
import com.anabada.anabada_api.domain.message.dto.MessageEntityDTO;
import com.anabada.anabada_api.domain.message.dto.RoomDTO;
import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.message.service.MessageFindService;
import com.anabada.anabada_api.domain.message.service.MessageUpdateService;

import com.anabada.anabada_api.domain.message.service.RoomFindService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<RoomDTO>> getMyRooms() {
        List<RoomVO> vos = roomFindService.getMyRooms();
        List<RoomDTO> dtos = vos.stream().map(RoomDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/rooms/{room-idx}/messages")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<MessageEntityDTO>> getMessagesByRoomIdx(
            @PathVariable("room-idx") Long roomIdx
    ) {

        List<MessageVO> vos = messageFindService.getMessagesByRoomIdx(roomIdx);
        List<MessageEntityDTO> dtos = vos.stream().map(MessageEntityDTO::withUserFromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/rooms/messages")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CreateMessage.Response> saveMessage(
            @RequestBody CreateMessage.Request request
    ) {
        MessageVO message = messageUpdateService.save(request);

        return new ResponseEntity<>(new CreateMessage.Response(message.getIdx()), HttpStatus.CREATED);
    }


}

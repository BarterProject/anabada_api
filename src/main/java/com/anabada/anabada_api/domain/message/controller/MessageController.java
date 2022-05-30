package com.anabada.anabada_api.domain.message.controller;


import com.anabada.anabada_api.domain.etc.dto.PageDTO;
import com.anabada.anabada_api.domain.message.dto.CreateMessage;
import com.anabada.anabada_api.domain.message.dto.MessageEntityDTO;
import com.anabada.anabada_api.domain.message.dto.RoomDTO;
import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.message.service.MessageFindService;
import com.anabada.anabada_api.domain.message.service.MessageUpdateService;

import com.anabada.anabada_api.domain.message.service.RoomFindService;
import com.anabada.anabada_api.firebase.FCMService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    FCMService fcmService;

    public MessageController(RoomFindService roomFindService, MessageFindService messageFindService, MessageUpdateService messageUpdateService, FCMService fcmService) {
        this.roomFindService = roomFindService;
        this.messageFindService = messageFindService;
        this.messageUpdateService = messageUpdateService;
        this.fcmService = fcmService;
    }

    @GetMapping("/v2/rooms")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<RoomDTO>> getMyRooms() {
        List<RoomVO> vos = roomFindService.getMyRooms();
        List<RoomDTO> dtos = vos.stream().map(RoomDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/v2/rooms/{room-idx}/messages")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<PageDTO<MessageEntityDTO>> getMessagesByRoomIdx(
            @PathVariable("room-idx") Long roomIdx,
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        Page<MessageVO> page = messageFindService.getMessagesByRoomIdx(roomIdx, pageable);
        List<MessageEntityDTO> dtos = page.getContent().stream().map(MessageEntityDTO::withUserFromEntity).collect(Collectors.toList());

        PageDTO<MessageEntityDTO> pageDTO = PageDTO.<MessageEntityDTO>builder()
                .contents(dtos)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @PostMapping("/rooms/messages")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CreateMessage.Response> saveMessage(
            @RequestBody CreateMessage.Request request
    ) {
        MessageVO message = messageUpdateService.save(request);

        fcmService.sendMessage(message);
        return new ResponseEntity<>(new CreateMessage.Response(message.getIdx()), HttpStatus.CREATED);
    }


}

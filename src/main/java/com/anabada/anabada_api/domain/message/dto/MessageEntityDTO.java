package com.anabada.anabada_api.domain.message.dto;


import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.user.dto.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageEntityDTO {

    private Long idx;

    private String content;

    LocalDateTime createdAt;

    private int state;

    private UserDTO sender;

    private RoomDTO room;

    @Builder
    public MessageEntityDTO(Long idx, String content, LocalDateTime createdAt, int state, UserDTO sender, RoomDTO room) {
        this.idx = idx;
        this.content = content;
        this.createdAt = createdAt;
        this.state = state;
        this.sender = sender;
        this.room = room;
    }

    public static MessageEntityDTO withUserFromEntity(MessageVO vo){
        return MessageEntityDTO.builder()
                .idx(vo.getIdx())
                .content(vo.getContent())
                .createdAt(vo.getCreatedAt())
                .state(vo.getState())
                .sender(UserDTO.simpleFromEntity(vo.getSender()))
                .build();

    }
}

package com.anabada.anabada_api.dto;


import com.anabada.anabada_api.dto.room.RoomDTO;
import com.anabada.anabada_api.dto.user.UserDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomUserMappingDTO {

    private Long idx;

    private UserDTO user;

    private RoomDTO room;

    public RoomUserMappingDTO(Long idx, UserDTO user, RoomDTO room) {
        this.idx = idx;
        this.user = user;
        this.room = room;
    }
}

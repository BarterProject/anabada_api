package com.anabada.anabada_api.domain.message;


import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.room.RoomUserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "ROOM_USER_TB")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomUserVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "user_idx_fk", updatable = false, nullable = false)
    private UserVO user;

    @ManyToOne
    @JoinColumn(name = "room_idx_fk", updatable = false, nullable = false)
    private RoomVO room;

    @Builder
    public RoomUserVO(UserVO user, RoomVO room) {
        this.room = room;
        this.user = user;
    }

    public RoomUserDTO dto(boolean user, boolean room) {
        return RoomUserDTO.builder()
                .idx(idx)
                .user(user ? this.user.dto() : null)
                .room(room ? this.room.dto(true) : null)
                .build();
    }



}

package com.anabada.anabada_api.domain.message;


import com.anabada.anabada_api.domain.user.UserVO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "ROOM_USER_TB")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomUserMappingVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "user_idx_fk", updatable = false, nullable = false)
    private UserVO user;

    @ManyToOne
    @JoinColumn(name = "room_idx_fk", updatable = false, nullable = false)
    private RoomVO room;


}

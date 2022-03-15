package com.anabada.anabada_api.domain.message;


import com.anabada.anabada_api.domain.user.UserVO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "MESSAGE_TB")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Lob
    @Column(name = "content", updatable = false, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @Column(name = "state", updatable = true, nullable = true)
    private int state;

    @ManyToOne
    @JoinColumn(name = "sender_idx_fk", updatable = false, nullable = false)
    private UserVO sender;

    @ManyToOne
    @JoinColumn(name = "room_idx_fk", updatable = false, nullable = false)
    private RoomVO room;


}

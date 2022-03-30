package com.anabada.anabada_api.domain.message;



import com.anabada.anabada_api.dto.DeliveryDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "ROOM_TB")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @Column(name = "state", updatable = true, nullable = true)
    private int state;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    @Column(name = "sender", updatable = false, nullable = false)
    private String sender;

    @Column(name = "receiver", updatable = false, nullable = false)
    private String receiver;

    @Builder
    public RoomVO(String name,String sender,String receiver,int state){
        this.name=name;
        this.sender=sender;
        this.receiver=receiver;
        this.state=state;
    }

}

package com.anabada.anabada_api.domain.message;



import com.anabada.anabada_api.domain.DeliveryVO;
import com.anabada.anabada_api.dto.DeliveryDTO;
import com.anabada.anabada_api.dto.RoomDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_idx_fk", nullable = false, updatable = true)
    private DeliveryVO delivery;

    @OneToMany(mappedBy = "room")
    private List<RoomUserMappingVO> mappings;


    @Builder
    public RoomVO(String name, String sender, String receiver, int state,DeliveryVO delivery) {
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
        this.delivery=delivery;
    }

    public RoomDTO dto(boolean delivery){
        return RoomDTO.builder()
                .idx(idx)
                .createdAt(createdAt)
                .state(state)
                .name(name)
                .delivery(delivery ? this.delivery.dto(false) : null)
                .build();
    }

}

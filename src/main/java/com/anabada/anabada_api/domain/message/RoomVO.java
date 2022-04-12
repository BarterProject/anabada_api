package com.anabada.anabada_api.domain.message;


import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.dto.room.RoomDTO;
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
    @Column(name = "idx", updatable = false,nullable = false)
    private Long idx;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @Column(name = "state", updatable = true, nullable = true)
    private int state;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_idx_fk", nullable = false, updatable = true)
    private DeliveryVO delivery;

    @OneToMany(mappedBy = "room")
    private List<RoomUserVO> mappings;


    @Builder
    public RoomVO(String name, int state, DeliveryVO delivery) {
        this.name = name;
        this.state = state;
        this.delivery = delivery;
    }

    public void setDelivery(DeliveryVO delivery) {
        this.delivery = delivery;
    }


    public RoomDTO dto(boolean delivery) {
        return RoomDTO.builder()
                .idx(idx)
                .createdAt(createdAt)
                .state(state)
                .name(name)
                .delivery(delivery ? this.delivery.dto(false) : null)
                .build();
    }



}

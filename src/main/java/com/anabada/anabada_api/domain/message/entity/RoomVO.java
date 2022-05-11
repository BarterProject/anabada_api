package com.anabada.anabada_api.domain.message.entity;


import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.message.dto.RoomDTO;
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


    public enum STATE{
        DEACTIVATED,
        ACTIVATED,
        TERMINATED
    }

    @Builder
    public RoomVO(String name, int state, DeliveryVO delivery) {
        this.name = name;
        this.state = state;
        this.delivery = delivery;
    }

    public void setDelivery(DeliveryVO delivery) {
        this.delivery = delivery;
    }



}

package com.anabada.anabada_api.domain.message.dto;


import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDTO {

    private  LocalDateTime createdAt;

    private Long idx;

    private int state;

    private String name;

    private DeliveryDTO delivery;
    @Builder
    public RoomDTO(Long idx, LocalDateTime createdAt, int state, String name,DeliveryDTO delivery){
        this.idx=idx;
        this.name=name;
        this.state=state;
        this.createdAt=createdAt;
        this.delivery=delivery;
    }

    public static RoomDTO fromEntity(RoomVO vo){
        return RoomDTO.builder()
                .idx(vo.getIdx())
                .state(vo.getState())
                .name(vo.getName())
                .build();
    }
}

package com.anabada.anabada_api.dto;



import com.anabada.anabada_api.domain.DeliveryVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDTO {

    private Long idx;

    LocalDateTime createdAt;

    private int state;

    private String name;

    private String sender;

    private String receiver;

    private DeliveryDTO delivery;

    private List<RoomUserMappingDTO> mappings;

    @Builder
    public RoomDTO(Long idx, LocalDateTime createdAt, int state, String name, String sender, String receiver, DeliveryDTO delivery, List<RoomUserMappingDTO> mappings) {
        this.idx = idx;
        this.createdAt = createdAt;
        this.state = state;
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
        this.delivery = delivery;
        this.mappings = mappings;
    }
}

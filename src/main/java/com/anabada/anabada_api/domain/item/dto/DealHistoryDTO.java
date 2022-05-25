package com.anabada.anabada_api.domain.item.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class DealHistoryDTO {
    String name;
    Long deposit;
    List<ItemImageDTO> itemImages;
    LocalDateTime tradedAt;

    @Builder
    public DealHistoryDTO(String name, Long deposit, List<ItemImageDTO> itemImages, LocalDateTime tradedAt) {
        this.name = name;
        this.deposit = deposit;
        this.itemImages = itemImages;
        this.tradedAt = tradedAt;
    }

}

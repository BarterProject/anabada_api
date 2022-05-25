package com.anabada.anabada_api.domain.item.dto;


import com.anabada.anabada_api.domain.item.entity.ItemVO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class DealHistoryDTO {
    String name;
    Long deposit;
    LocalDateTime tradedAt;
    List<ItemImageDTO> itemImages;

    @Builder
    public DealHistoryDTO(LocalDateTime tradedAt, String name, Long deposit, List<ItemImageDTO> itemImages) {
        this.tradedAt = tradedAt;
        this.name = name;
        this.deposit = deposit;
        this.itemImages = itemImages;
    }

    public static DealHistoryDTO fromEntity(ItemVO vo) {
        return DealHistoryDTO.builder()
                .name(vo.getName())
                .deposit(vo.getDeposit())
                .tradedAt(vo.getCreatedAt())
                .itemImages(vo.getImages().stream().limit(1).map(ItemImageDTO::fromEntity).collect(Collectors.toList()))
                .build();
    }


}

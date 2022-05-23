package com.anabada.anabada_api.domain.item.dto;


import com.anabada.anabada_api.domain.item.entity.DealRequestVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class DealItemDTO {
    String name;
    Long deposit;
    LocalDateTime tradedAt;
    List<ItemImageDTO> itemImages;


    @Builder
    public DealItemDTO(LocalDateTime tradedAt, String name, Long deposit, List<ItemImageDTO> itemImages) {
        this.tradedAt = tradedAt;
        this.name = name;
        this.deposit = deposit;
        this.itemImages = itemImages;
    }

    public static DealItemDTO fromEntityRequest(DealRequestVO vo) {
        return DealItemDTO.builder()
                .name(vo.getRequestItem().getName())
                .deposit(vo.getRequestItem().getDeposit())
                .itemImages(
                        vo.getRequestItem().getImages().stream().map(ItemImageDTO::fromEntity).collect(Collectors.toList())
                )
                .build();
    }

    public static DealItemDTO fromEntityResponse(DealRequestVO vo) {
        return DealItemDTO.builder()
                .name(vo.getResponseItem().getName())
                .deposit(vo.getRequestItem().getDeposit())
                .itemImages(
                        vo.getRequestItem().getImages().stream().map(ItemImageDTO::fromEntity).collect(Collectors.toList())
                )
                .build();
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        List<DealItemDTO> requestItem;
        List<DealItemDTO> responseItem;
    }


}

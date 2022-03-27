package com.anabada.anabada_api.dto;

import com.anabada.anabada_api.dto.item.ItemDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealRequestDTO {

    private Long idx;

    private LocalDateTime createdAt;

    private Long state;

    private LocalDateTime tradedAt;

    @NotNull(groups = {ValidationGroups.dealRequestGroup.class}, message = "요청 아이템이 입력되지 않았습니다.")
    ItemDTO requestItem;

    @NotNull(groups = {ValidationGroups.dealRequestGroup.class}, message = "요청 대상 아이템이 입력되지 않았습니다.")
    ItemDTO responseItem;

    @Builder
    public DealRequestDTO(Long idx, LocalDateTime createdAt, Long state, LocalDateTime tradedAt, ItemDTO requestItem, ItemDTO responseItem) {
        this.idx = idx;
        this.createdAt = createdAt;
        this.state = state;
        this.tradedAt = tradedAt;
        this.requestItem = requestItem;
        this.responseItem = responseItem;
    }
}

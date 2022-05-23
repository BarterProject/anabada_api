package com.anabada.anabada_api.domain.item.dto;

import com.anabada.anabada_api.domain.etc.dto.ValidationGroups;
import com.anabada.anabada_api.domain.item.entity.DealRequestVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealRequestDTO {

    @NotNull(groups = {ValidationGroups.dealRequestGroup.class}, message = "요청 아이템이 입력되지 않았습니다.")
    ItemDTO requestItem;
    @NotNull(groups = {ValidationGroups.dealRequestGroup.class}, message = "요청 대상 아이템이 입력되지 않았습니다.")
    ItemDTO responseItem;
    private Long idx;
    private LocalDateTime createdAt;
    private int state;
    private LocalDateTime tradedAt;

    @Builder
    public DealRequestDTO(Long idx, LocalDateTime createdAt, int state, LocalDateTime tradedAt, ItemDTO requestItem, ItemDTO responseItem) {
        this.idx = idx;
        this.createdAt = createdAt;
        this.state = state;
        this.tradedAt = tradedAt;
        this.requestItem = requestItem;
        this.responseItem = responseItem;
    }

    public static DealRequestDTO fromEntity(DealRequestVO vo) {
        return DealRequestDTO.builder()
                .idx(vo.getIdx())
                .createdAt(vo.getCreatedAt())
                .state(vo.getState())
                .tradedAt(vo.getTradedAt())
                .responseItem(ItemDTO.listFromEntity(vo.getResponseItem()))
                .requestItem(ItemDTO.listFromEntity(vo.getRequestItem()))
                .build();
    }


}

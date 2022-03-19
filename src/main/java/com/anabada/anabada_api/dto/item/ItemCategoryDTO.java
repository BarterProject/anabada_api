package com.anabada.anabada_api.dto.item;


import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.ValidationGroups;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategoryDTO {
    private Long idx;

    private ItemCategoryDTO upperCategory;

    @NotBlank(groups = {ValidationGroups.categorySaveGroup.class}, message = "카테고리 명이 등록되지 않았습니다.")
    private String name;


    @Builder
    public ItemCategoryDTO(Long idx, ItemCategoryDTO upperCategory, String name) {
        this.idx = idx;
        this.upperCategory = upperCategory;
        this.name = name;
    }


    public ItemCategoryVO toEntity() {
        return ItemCategoryVO.builder()
                .name(this.name)
                .build();
    }


}

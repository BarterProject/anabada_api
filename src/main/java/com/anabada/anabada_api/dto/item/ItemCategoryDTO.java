package com.anabada.anabada_api.dto.item;


import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategoryDTO {
    private Long idx;

    private ItemVO item;

    private ItemCategoryVO itemCategories;

    private String name;


    @Builder
    public ItemCategoryDTO(Long idx, ItemCategoryVO itemCategories, String name, ItemVO item) {
        this.idx = idx;
        this.itemCategories = itemCategories;
        this.name = name;
        this.item = item;
    }

    public ItemCategoryVO toEntity() {
        return ItemCategoryVO.builder()
                .itemCategories(this.itemCategories)
                .name(this.name)
                .build();
    }


}

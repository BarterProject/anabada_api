package com.anabada.anabada_api.domain.item;

import com.anabada.anabada_api.dto.item.ItemCategoryDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "ITEM_CATEGORY_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategoryVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Column(name = "name", updatable = false, nullable = true,length = 100)
    String name;

    @ManyToOne
    @JoinColumn(name = "upper_category_idx", updatable = true, nullable = true)
    private ItemCategoryVO upperCategory;

    @OneToMany(mappedBy = "itemCategory")
    List<ItemVO> itemList;

    @Builder
    public ItemCategoryVO(String name) {
        this.name = name;
    }

    public void setUpperCategory(ItemCategoryVO upperCategory) {
        this.upperCategory = upperCategory;
    }

    public ItemCategoryDTO dto(){
        return ItemCategoryDTO.builder()
                .idx(idx)
                .name(name)
                .upperCategory(upperCategory != null ? upperCategory.dto() : null)
                .build();
    }

}

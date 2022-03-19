package com.anabada.anabada_api.domain.item;

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

    @OneToOne(mappedBy = "itemCategory", cascade = CascadeType.ALL)
    ItemVO item;

    @ManyToOne
    @JoinColumn(name = "upper_category_idx", updatable = true, nullable = true)
    private ItemCategoryVO itemCategories;

    @Builder
    public ItemCategoryVO(String name,ItemVO item,ItemCategoryVO itemCategories){
        this.name=name;
        this.item=item;
        this.itemCategories=itemCategories;
    }

}

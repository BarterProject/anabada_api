package com.anabada.anabada_api.domain.item;

import lombok.AccessLevel;
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

    @Column(name = "upper_category_idx", updatable = true, nullable = true)
    Long upperCategoryIdx;

    @Column(name = "name", updatable = false, nullable = true,length = 100)
    String name;

    @OneToOne(mappedBy = "itemCategory", cascade = CascadeType.ALL)
    ItemVO item;

    @OneToMany(mappedBy = "itemCategory",cascade = CascadeType.ALL)
    private List<ItemCategoryVO> itemCategories;

}

package com.anabada.anabada_api.domain.item.entity;

import com.anabada.anabada_api.domain.etc.entity.FileInfo;
import com.anabada.anabada_api.domain.item.dto.ItemImageDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "ITEM_IMAGE_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImageVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Column(name = "name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "number", updatable = true, nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx_fk", updatable = false, nullable = false)
    ItemVO item;

    @Embedded
    private FileInfo fileInfo;

    @Builder
    public ItemImageVO(String name, FileInfo fileInfo, Long number) {
        this.name = name;
        this.fileInfo = fileInfo;
        this.number = number;
    }

    public ItemImageDTO dto(){
        return ItemImageDTO.builder()
                .idx(idx)
                .extension(fileInfo.getExtension())
                .number(number)
                .originalName(fileInfo.getOriginalName())
                .saveName(fileInfo.getSaveName())
                .uploadPath("*")
                .name(name)
                .createdAt(createdAt)
                .size(fileInfo.getSize())
                .build();
    }

    public void setItem(ItemVO item) {
        this.item = item;
    }

}


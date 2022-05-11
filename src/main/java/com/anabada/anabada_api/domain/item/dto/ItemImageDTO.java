package com.anabada.anabada_api.domain.item.dto;

import com.anabada.anabada_api.domain.item.entity.ItemImageVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImageDTO {

    private Long idx;

    private String name;

    private LocalDateTime createdAt;

    private Long number;

    private String originalName;

    private String saveName;

    private Long size;

    private String uploadPath;

    private String extension;

    @Builder
    public ItemImageDTO(Long idx, String name, LocalDateTime createdAt, Long number, String originalName, String saveName, Long size, String uploadPath, String extension) {
        this.idx = idx;
        this.name = name;
        this.createdAt = createdAt;
        this.number = number;
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.uploadPath = uploadPath;
        this.extension = extension;
    }

    public static ItemImageDTO fromEntity(ItemImageVO vo){
        return ItemImageDTO.builder()
                .name(vo.getName())
                .extension(vo.getFileInfo().getExtension())
                .size(vo.getFileInfo().getSize())
                .build();
    }
}







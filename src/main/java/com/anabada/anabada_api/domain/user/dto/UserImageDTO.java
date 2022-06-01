package com.anabada.anabada_api.domain.user.dto;


import com.anabada.anabada_api.domain.user.entity.UserImageVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImageDTO {
    private Long idx;

    private LocalDateTime createdAt;

    private String name;

    private String originalName;

    private String saveName;

    private Long size;

    private String uploadPath;

    private String extension;

    @Builder
    public UserImageDTO(Long idx, String name, LocalDateTime createdAt, String originalName, String saveName, Long size, String uploadPath, String extension) {
        this.idx = idx;
        this.name = name;
        this.createdAt = createdAt;
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.uploadPath = uploadPath;
        this.extension = extension;
    }

    public static UserImageDTO fromEntity(UserImageVO vo) {
        return UserImageDTO.builder()
                .name(vo.getName())
                .extension(vo.getFileInfo().getExtension())
                .size(vo.getFileInfo().getSize())
                .build();
    }

}

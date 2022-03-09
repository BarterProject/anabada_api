package com.anabada.anabada_api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInfo {

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "save_name")
    private String saveName;

    @Column(name = "size")
    private Long size;

    @Column(name = "upload_path")
    private String uploadPath;

    @Column(name = "extension",length = 45)
    private String extension;

    @Builder
    public FileInfo(String originalName, String saveName, Long size, String uploadPath, String extension) {
        this.originalName = originalName;
        this.saveName = saveName;
        this.size = size;
        this.uploadPath = uploadPath;
        this.extension = extension;
    }
}

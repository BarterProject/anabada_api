package com.anabada.anabada_api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "COMMON_IMAGE_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonImageVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private FileInfo fileInfo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public CommonImageVO(Long idx, String name, FileInfo fileInfo) {
        this.idx = idx;
        this.name = name;
        this.fileInfo = fileInfo;
    }

}

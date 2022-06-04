package com.anabada.anabada_api.domain.user.entity;

import com.anabada.anabada_api.domain.etc.entity.FileInfo;
import com.anabada.anabada_api.domain.user.dto.UserImageDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "USER_IMAGE_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImageVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "name")
    private String name;

    @Embedded
    private FileInfo fileInfo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userImage")
    private UserVO user;

    @Builder
    public UserImageVO(String name, FileInfo fileInfo) {
        this.name = name;
        this.fileInfo = fileInfo;
    }

    public UserImageDTO dto() {
        return UserImageDTO.builder()
                .idx(idx)
                .name(name)
                .extension(fileInfo.getExtension())
                .originalName(fileInfo.getOriginalName())
                .saveName(fileInfo.getSaveName())
                .uploadPath("*")
                .createdAt(createdAt)
                .size(fileInfo.getSize())
                .build();
    }

    public void setUser(UserVO user) {
        this.user = user;
        user.setUserImage(this);
    }



}

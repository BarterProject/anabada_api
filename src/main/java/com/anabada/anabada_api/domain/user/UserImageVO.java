package com.anabada.anabada_api.domain.user;

import com.anabada.anabada_api.domain.FileInfo;
import com.anabada.anabada_api.domain.item.ItemVO;
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

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Embedded
    private FileInfo fileInfo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userImage")
    private UserVO user;

    @Builder
    public UserImageVO(Long idx, String name, FileInfo fileInfo, LocalDateTime createdAt) {
        this.idx = idx;
        this.name = name;
        this.fileInfo = fileInfo;
    }

    public void setUser(UserVO user) {
        this.user = user;
        user.setUserImage(this);
    }


}

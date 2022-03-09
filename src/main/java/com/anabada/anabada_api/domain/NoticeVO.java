package com.anabada.anabada_api.domain;

import com.anabada.anabada_api.domain.user.UserVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Lob
    @Column(name = "content", updatable = true, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "state", updatable = true, nullable = true)
    private Long state;

    @Column(name = "router", updatable = true, nullable = true)
    private String router;

    @Column(name = "kind", updatable = true, nullable = true, length = 45)
    private String kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx_fk", nullable = false, updatable = false)
    UserVO user;

    @Builder
    public NoticeVO(String content, Long state, String kind, UserVO user) {
        this.content = content;
        this.state = state;
        this.kind = kind;
        this.user = user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}

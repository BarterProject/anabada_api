package com.anabada.anabada_api.domain.message.entity;

import com.anabada.anabada_api.domain.user.entity.UserVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "NOTICE_TB")
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

    @Column(name = "route", updatable = true, nullable = true)
    private String route;

    @Column(name = "kind", updatable = true, nullable = true, length = 45)
    private String kind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx_fk", nullable = false, updatable = false)
    UserVO user;

    @Builder
    public NoticeVO(String content, Long state, String route, String kind, UserVO user) {
        this.content = content;
        this.state = state;
        this.route = route;
        this.kind = kind;
        this.user = user;
    }


    public void setUser(UserVO user) {
        this.user = user;
    }
}

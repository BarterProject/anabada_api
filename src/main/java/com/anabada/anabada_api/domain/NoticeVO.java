package com.anabada.anabada_api.domain;

import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.ReportDTO;
import com.anabada.anabada_api.dto.user.NoticeDTO;
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

    public NoticeDTO dto(boolean user){
        return NoticeDTO.builder()
                .idx(this.idx)
                .createdAt(this.createdAt)
                .user(user ? this.user.dto(false) : null)
                .content(this.content)
                .state(this.state)
                .kind(this.kind)
                .route(this.route)
                .build();
    }


    public void setUser(UserVO user) {
        this.user = user;
    }
}

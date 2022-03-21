package com.anabada.anabada_api.domain;

import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.ReportDTO;
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
    @Column(name = "title",updatable = true,nullable = false)
    private String title;

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
    public NoticeVO(String title,String content, Long state, String kind, UserVO user) {
        this.title=title;
        this.content = content;
        this.state = state;
        this.kind = kind;
        this.user = user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }



}

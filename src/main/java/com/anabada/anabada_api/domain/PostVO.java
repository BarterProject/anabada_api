package com.anabada.anabada_api.domain;

import com.anabada.anabada_api.domain.user.UserVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "POST_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Column(name = "title", updatable = true, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", updatable = true, nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx_fk")
    UserVO user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx_fk")
    BoardVO board;

    @Builder
    public PostVO(String title, String content,  UserVO user, BoardVO board) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;
    }


}

package com.anabada.anabada_api.domain;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "REPORT_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false)
    private Long idx;

    @Lob
    @Column(name = "content", nullable = false, updatable = true)
    private String content;

    @Lob
    @Column(name = "reply", nullable = true, updatable = true)
    private String reply;

    @Column(name = "state", nullable = false, updatable = true)
    private Long state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx_fk", nullable = false, updatable = true)
    UserVO user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx_fk", nullable = true, updatable = true)
    ItemVO item;

    @Builder
    public ReportVO(String content, String reply, Long state, UserVO user, ItemVO item) {
        this.content = content;
        this.state = state;
        this.reply = reply;
        this.user = user;
        this.item = item;
    }

}

package com.anabada.anabada_api.domain.etc.entity;

import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
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

    @Column(name = "title", nullable = false, updatable = true, length = 45)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, updatable = true)
    private String content;

    @Lob
    @Column(name = "reply", nullable = true, updatable = true)
    private String reply;

    @Column(name = "state", nullable = false, updatable = true)
    private int state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_idx_fk", nullable = false, updatable = true)
    UserVO user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_idx_fk", nullable = true, updatable = true)
    ItemVO item;


    public enum STATE {
        DEACTIVATED,
        APPLIED,
        COMPLETED
    }

    @Builder
    public ReportVO(String title, String content, String reply, int state, UserVO user, ItemVO item) {
        this.title = title;
        this.content = content;
        this.state = state;
        this.reply = reply;
        this.user = user;
        this.item = item;
    }

    public void updateState(int state) {
        this.state = state;
    }


    public void setItem(ItemVO item) {
        this.item = item;
    }
}

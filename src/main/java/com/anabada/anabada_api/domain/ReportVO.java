package com.anabada.anabada_api.domain;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.ReportDTO;
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

    @Column(name = "title",nullable = false,updatable = true,length = 45)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, updatable = true)
    private String content;

    @Lob
    @Column(name = "reply", nullable = true, updatable = true)
    private String reply;

    @Column(name = "state", nullable = false, updatable = true)
    private Long state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_idx_fk", nullable = false, updatable = true)
    UserVO user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_idx_fk", nullable = true, updatable = true)
    ItemVO item;

    @Builder
    public ReportVO(String title,String content, String reply, Long state, UserVO user, ItemVO item) {
        this.title=title;
        this.content = content;
        this.state = state;
        this.reply = reply;
        this.user = user;
        this.item = item;
    }

    public ReportDTO dto(boolean item, boolean user) {
        return ReportDTO.builder()
                .idx(idx)
                .title(title)
                .content(content)
                .reply(reply)
                .state(state)
                .item(item ? this.item.dto(true,true,true,true,true):null)
                .user(user?this.user.dto():null)
                .build();
    }

    public void updateByUser(String title,String content){
        this.title=title;
        this.content=content;
    }

    public void updateByAdmin(Long state,String reply){
        this.state=state;
        this.reply=reply;
    }

    public void setItem(ItemVO item) {
        this.item = item;
    }
}

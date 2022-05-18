package com.anabada.anabada_api.domain.item.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "DEAL_REQUEST_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DealRequestVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "state", updatable = true, nullable = true)
    private int state;

    @Column(name = "traded_at", updatable = true, nullable = true)
    private LocalDateTime tradedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_item_idx_fk")
    ItemVO requestItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "response_item_idx_fk")
    ItemVO responseItem;

    public void setState(int state) {
        this.state = state;
    }

    public enum STATE{
        DEACTIVATED, ACTIVATED, CLOSED, ACCOMPLISHED, DENIED
    }

    @Builder
    public DealRequestVO(int state, LocalDateTime tradedAt, ItemVO requestItem, ItemVO responseItem) {

        this.tradedAt = tradedAt;
        this.state = state;
        this.requestItem = requestItem;
        this.responseItem = responseItem;
    }

    public void close(){
        this.state = STATE.CLOSED.ordinal();
    }
}

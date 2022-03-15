package com.anabada.anabada_api.domain;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
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
    @Column(name = "created_at", updatable = false,nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "state", updatable = true, nullable = true)
    private Long state;

    @Column(name = "traded_at", updatable = true, nullable = true)
    private LocalDateTime tradedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    ItemVO requestItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    ItemVO responseItem;

    @Builder
    public DealRequestVO(Long state, LocalDateTime tradedAt, ItemVO requestItem, ItemVO responseItem) {

        this.tradedAt = tradedAt;
        this.state = state;
        this.requestItem = requestItem;
        this.responseItem = responseItem;
    }

    public void setRequestItem(ItemVO requestItem) {
        this.requestItem = requestItem;
    }

    public void setResponseItem(ItemVO responseItem) {
        this.responseItem = responseItem;
    }
}

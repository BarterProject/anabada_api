package com.anabada.anabada_api.domain.pay.entity;


import com.anabada.anabada_api.domain.item.entity.ItemVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "PAYMENT_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Column(name = "amount", updatable = true, nullable = true)
    private Long amount;

    @Column(name = "state", updatable = true, nullable = true)
    private int state;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    ItemVO item;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "option_idx_fk", nullable = false, updatable = true)
    PaymentOptionVO paymentOption;


    @Builder
    public PaymentVO(Long amount, int state, PaymentOptionVO paymentOption) {
        this.amount = amount;
        this.state = state;
        this.paymentOption = paymentOption;
    }


    public enum STATE{
        INACTIVATED, ACTIVATED;
    }
}

package com.anabada.anabada_api.domain.pay;


import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
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
    private Long state;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    ItemVO item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_idx_fk", nullable = false, updatable = true)
    PaymentOptionVO paymentOption;


    @Builder
    public PaymentVO(Long amount, Long state, PaymentOptionVO paymentOption) {
        this.amount = amount;
        this.state = state;
        this.paymentOption = paymentOption;
    }

    public PaymentDTO dto() {
        return PaymentDTO.builder()
                .idx(idx)
                .createdAt(createdAt)
                .amount(amount)
                .build();
    }

    public void setItem(ItemVO item) {
        this.item = item;
    }


    public void update(Long amount, Long state, PaymentOptionVO paymentOption) {
        this.amount = amount;
        this.state = state;
        this.paymentOption = paymentOption;
    }


}

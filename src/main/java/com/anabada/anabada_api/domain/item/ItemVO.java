package com.anabada.anabada_api.domain.item;

import com.anabada.anabada_api.domain.DeliveryVO;
import com.anabada.anabada_api.domain.DealRequestVO;
import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;
import com.anabada.anabada_api.domain.user.UserVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "ITEM_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Column(name = "name", updatable = true, nullable = true, length = 100)
    private String name;

    @Column(name = "description", updatable = true, nullable = true, length = 100)
    private String description;

    @Column(name = "deposit", updatable = true, nullable = true)
    private Long deposit;

    @Column(name = "clause_agree", updatable = true, nullable = true)
    boolean clauseAgree;

    @Column(name = "state", updatable = true, nullable = true)
    private Long state;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "end_at", updatable = true, nullable = true)
    private LocalDateTime endAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_idx_fk", nullable = false, updatable = true)
    PaymentVO payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_idx_fk", nullable = true, updatable = true)
    DeliveryVO delivery;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx_fk", nullable = true, updatable = true)
    ItemCategoryVO itemCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrant_idx_fk", nullable = false, updatable = false)
    UserVO registrant;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_idx_fk", nullable = false, updatable = true)
    UserVO owner;

    @OneToMany(mappedBy = "requestItem", fetch = FetchType.LAZY)
    List<DealRequestVO> dealRequestItemList;

    @OneToMany(mappedBy = "responseItem", fetch = FetchType.LAZY)
    List<DealRequestVO> dealResponseItemList;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportVO> reports = new ArrayList<>();

    public void setDelivery(DeliveryVO delivery) {
        this.delivery = delivery;
    }

    @Builder
    public ItemVO(String name, String description, Long deposit, boolean clauseAgree, Long state, PaymentVO payment, UserVO registrant, UserVO owner) {
        this.name = name;
        this.description = description;
        this.deposit = deposit;
        this.clauseAgree = clauseAgree;
        this.state = state;
        this.payment = payment;
        this.registrant = registrant;
        this.owner = owner;
    }
}

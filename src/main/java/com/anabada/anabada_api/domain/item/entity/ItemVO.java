package com.anabada.anabada_api.domain.item.entity;

import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.etc.entity.ReportVO;
import com.anabada.anabada_api.domain.pay.entity.PaymentVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
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
    private int state;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "end_at", updatable = true, nullable = true)
    private LocalDateTime endAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_idx_fk", nullable = false, updatable = true)
    PaymentVO payment;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_idx_fk", nullable = true, updatable = true, unique = true)
    DeliveryVO delivery;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_idx_fk", nullable = true, updatable = true)
    ItemCategoryVO itemCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrant_idx_fk", nullable = false, updatable = false)
    UserVO registrant;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_idx_fk", nullable = false, updatable = true)
    UserVO owner;

    @OneToMany(mappedBy = "requestItem", fetch = FetchType.LAZY)
    List<DealRequestVO> dealRequestItemList = new ArrayList<>();

    @OneToMany(mappedBy = "responseItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<DealRequestVO> dealResponseItemList = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReportVO> reports = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    List<ItemImageVO> images = new ArrayList<>();


    public enum STATE {
        INACTIVATED,
        APPLIED,
        EXCHANGING,
        TERMINATED,
        WAITING,
        REFUND,
        REFUNDEND,
        RETURN
    }

    public void requestRefund() {
        this.state = STATE.REFUND.ordinal();
    }

    public void refund() {
        this.state = STATE.REFUNDEND.ordinal();
    }

    public void requestDeposit(){
        this.state=STATE.RETURN.ordinal();
    }

    public void setDelivery(DeliveryVO delivery) {
        this.delivery = delivery;
        this.state = STATE.EXCHANGING.ordinal();
        delivery.setItem(this);
    }

    public void addImage(ItemImageVO image) {
        this.images.add(image);
        image.setItem(this);
    }

    public void changeOwner(UserVO _owner) {
        this.owner = _owner;
    }

    public void deleteCategory(ItemCategoryVO defaultCategory){
        this.itemCategory = defaultCategory;
    }

    public void activate(boolean isActivated){
        this.state = isActivated ? 1 : 0;
    }


    @Builder
    public ItemVO(String name, String description, Long deposit, boolean clauseAgree, int state, PaymentVO payment, ItemCategoryVO itemCategory, UserVO registrant, UserVO owner, DeliveryVO delivery) {
        this.name = name;
        this.description = description;
        this.deposit = deposit;
        this.clauseAgree = clauseAgree;
        this.state = state;
        this.payment = payment;
        this.itemCategory = itemCategory;
        this.registrant = registrant;
        this.owner = owner;
        this.delivery = delivery;
    }



}

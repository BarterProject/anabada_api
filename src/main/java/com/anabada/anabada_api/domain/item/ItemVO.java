package com.anabada.anabada_api.domain.item;

import com.anabada.anabada_api.domain.DealRequestVO;
import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.item.ItemDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "end_at", updatable = true, nullable = true)
    private LocalDateTime endAt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_idx_fk", nullable = false, updatable = true)
    PaymentVO payment;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_idx_fk", nullable = true, updatable = true, unique = true)
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

    @OneToMany(mappedBy = "responseItem", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<DealRequestVO> dealResponseItemList;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReportVO> reports = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    List<ItemImageVO> images = new ArrayList<>();

    public void setDelivery(DeliveryVO delivery) {
        this.delivery = delivery;
        delivery.setItem(this);
    }


    public void setPayment(PaymentVO payment) {
        this.payment = payment;
    }

    public void addImage(ItemImageVO image) {
        this.images.add(image);
        image.setItem(this);
    }

    public void changeOwner(UserVO _owner) {
        this.owner = _owner;
    }


    @Builder
    public ItemVO(String name, String description, Long deposit, boolean clauseAgree, Long state, PaymentVO payment, ItemCategoryVO itemCategory, UserVO registrant, UserVO owner, DeliveryVO delivery) {
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

    public ItemDTO dto(Boolean registrant, Boolean owner, Boolean payment, Boolean category, Boolean images, Boolean delivery) {
        return ItemDTO.builder()
                .idx(idx)
                .name(name)
                .description(description)
                .deposit(deposit)
                .clause_agree(clauseAgree)
                .state(state)
                .payment(payment ? this.payment.dto(true) : null)
                .itemCategory(category ? (this.itemCategory != null ? this.itemCategory.dto() : null) : null)
                .registrant(registrant ? this.registrant.dto(true) : null)
                .images(images ? this.getImages().stream().map(ItemImageVO::dto).collect(Collectors.toList()) : null)
                .owner(owner ? this.owner.dto(true) : null)
                .createdAt(this.createdAt)
                .endAt(endAt)
                .delivery(delivery ? this.delivery != null ? this.delivery.dto(true,false,false) : null : null)
                .build();

    }
}

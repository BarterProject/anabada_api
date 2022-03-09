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
@Table(name = "DELIVERY_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Column(name = "address", updatable = true, nullable = true, length = 200)
    Long state;

    @Column(name = "phone", updatable = true, nullable = true, length = 50)
    String phone;

    @Column(name = "receiver_name", updatable = true, nullable = true, length = 50)
    String receiverName;

    @Column(name = "clause_agree", updatable = true, nullable = true)
    boolean clauseAgree;

    @Column(name = "tracking_number", updatable = true, nullable = true, length = 50)
    String trackingNumber;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    LocalDateTime createdAt;

    @Column(name = "due_at")
    LocalDateTime dueAt;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    ItemVO item;

    @Builder
    public DeliveryVO(Long state,String phone,String receiverName,boolean clauseAgree,String trackingNumber,LocalDateTime createdAt,LocalDateTime dueAt)
    {
    this.state=state;
    this.phone=phone;
    this.receiverName=receiverName;
    this.clauseAgree=clauseAgree;
    this.trackingNumber=trackingNumber;
    this.createdAt=createdAt;
    this.dueAt=dueAt;
    }




}

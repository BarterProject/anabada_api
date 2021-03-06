package com.anabada.anabada_api.domain.delivery.entity;

import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.util.CryptoConverter;
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

    @Column(name = "clause_agree", updatable = true, nullable = true)
    private boolean clauseAgree;

    @Column(name = "tracking_number", updatable = true, nullable = true, length = 50)
    private String trackingNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    ItemVO item;

    @Column(name = "address", updatable = true, nullable = false)
    @Convert(converter = CryptoConverter.class)
    private String address;

    @Column(name = "state", updatable = true, nullable = true, length = 200)
    private int state;

    @Column(name = "phone", updatable = true, nullable = true, length = 255)
    @Convert(converter = CryptoConverter.class)
    private String phone;

    @Column(name = "receiver_name", updatable = true, nullable = true, length = 255)
    @Convert(converter = CryptoConverter.class)
    private String receiverName;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private RoomVO room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_company_fk", updatable = true)
    private DeliveryCompanyVO deliveryCompany;

    @Builder
    public DeliveryVO(int state,
                      String address, String phone, String receiverName, boolean clauseAgree, String trackingNumber,
                      LocalDateTime dueAt, ItemVO item, DeliveryCompanyVO company) {
        this.state = state;
        this.phone = phone;
        this.receiverName = receiverName;
        this.clauseAgree = clauseAgree;
        this.trackingNumber = trackingNumber;
        this.dueAt = dueAt;
        this.item = item;
        this.address = address;
        this.deliveryCompany = company;
    }

    public enum STATE {
        INACTIVATED,
        APPLIED,
        EXCHANGING,
        TERMINATED
    }

    public void completeDelivery() {
        this.state = STATE.TERMINATED.ordinal();
    }

    public void setItem(ItemVO itemVO) {
        this.item = item;
    }

    public void setRoom(RoomVO room) {
        this.room = room;
        room.setDelivery(this);
    }

    public void setTrackingInfo(String trackingNumber, DeliveryCompanyVO deliveryCompany) {
        this.trackingNumber = trackingNumber;
        this.deliveryCompany = deliveryCompany;
        this.state = STATE.EXCHANGING.ordinal();
    }


}
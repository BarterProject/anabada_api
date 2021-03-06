package com.anabada.anabada_api.domain.pay.entity;

import com.anabada.anabada_api.domain.pay.dto.PaymentOptionDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "PAYMENT_OPTION_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentOptionVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, updatable = false)
    private Long idx;

    @Column(name = "name", updatable = true, nullable = true, length = 45)
    private String name;

    @Column(name = "description", updatable = true, nullable = true, length = 200)
    private String description;


    @Builder
    public PaymentOptionVO(String name, String description) {
        this.name = name;
        this.description = description;
    }


}

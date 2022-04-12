package com.anabada.anabada_api.domain.delivery;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "DELIVERY_COMPANY_TB")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryCompanyVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "code", updatable = false, nullable = false)
    private int code;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

   @OneToMany(mappedBy = "deliveryCompany", fetch = FetchType.LAZY)
   List<DeliveryVO> deliveries=new ArrayList<>();
}

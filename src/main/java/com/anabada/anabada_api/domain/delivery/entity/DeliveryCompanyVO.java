package com.anabada.anabada_api.domain.delivery.entity;


import com.anabada.anabada_api.domain.delivery.dto.DeliveryCompanyDTO;
import lombok.AccessLevel;
import lombok.Builder;
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
    private String code;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    @OneToMany(mappedBy = "deliveryCompany", fetch = FetchType.LAZY)
    List<DeliveryVO> deliveries=new ArrayList<>();


   @Builder
    public DeliveryCompanyVO(String code,String name) {
    this.code=code;
    this.name=name;
   }

   public DeliveryCompanyDTO dto(){
       return DeliveryCompanyDTO.builder()
               .idx(idx)
               .name(name)
               .code(code)
               .build();
   }


}

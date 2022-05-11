package com.anabada.anabada_api.domain.pay.dto;

import com.anabada.anabada_api.domain.pay.entity.PaymentOptionVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentOptionDTO {
    private Long idx;

    private String name;

    private String description;
    @Builder
    public PaymentOptionDTO(Long idx,String name,String description){
        this.idx=idx;
        this.name=name;
        this.description=description;
    }

    public static PaymentOptionDTO fromEntity(PaymentOptionVO vo){
        return PaymentOptionDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .description(vo.getDescription())
                .build();
    }


}

package com.anabada.anabada_api.dto.payment;

import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
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

    private PaymentDTO payment;

    @Builder
    public PaymentOptionDTO(Long idx,String name,String description,PaymentDTO payment){
        this.idx=idx;
        this.name=name;
        this.description=description;
        this.payment=payment;
    }

    public PaymentOptionVO toEntity(){
        return PaymentOptionVO.builder()
                .name(this.name)
                .description(this.description)
                .build();
    }

}

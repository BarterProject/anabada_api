package com.anabada.anabada_api.dto.payment;

import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;
import com.anabada.anabada_api.dto.ValidationGroups;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDTO {

    private Long idx;

    @NotNull(groups = {ValidationGroups.paymentGroup.class}, message = "금액이 입력되지 않았습니다.")
    private Long amount;

    private Long state;

    private LocalDateTime createdAt;

    @NotNull(groups = {ValidationGroups.paymentGroup.class}, message = "결제 옵션이 선택되지 않았습니다.")
    private PaymentOptionDTO paymentOption;


     @Builder
     public PaymentDTO(Long idx,Long amount,Long state,LocalDateTime createdAt,PaymentOptionDTO paymentOption){
         this.idx=idx;
         this.amount=amount;
         this.state=state;
         this.createdAt=createdAt;
         this.paymentOption=paymentOption;
     }

   public PaymentVO toEntity(PaymentOptionVO paymentOption){
        return PaymentVO.builder()
                .amount(this.amount)
                .state(this.state)
                .paymentOption(paymentOption)
                .build();


   }


}

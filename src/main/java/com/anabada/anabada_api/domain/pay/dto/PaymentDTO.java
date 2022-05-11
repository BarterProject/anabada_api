package com.anabada.anabada_api.domain.pay.dto;

import com.anabada.anabada_api.domain.pay.entity.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.entity.PaymentVO;
import com.anabada.anabada_api.domain.etc.dto.ValidationGroups;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDTO {

    private Long idx;

    @NotNull(groups = {ValidationGroups.paymentGroup.class}, message = "금액이 입력되지 않았습니다.")
    private Long amount;

    private int state;

    private LocalDateTime createdAt;

    @NotNull(groups = {ValidationGroups.paymentGroup.class}, message = "결제 옵션이 선택되지 않았습니다.")
    private PaymentOptionDTO paymentOption;


    @Builder
    public PaymentDTO(Long idx, Long amount, int state, LocalDateTime createdAt, PaymentOptionDTO paymentOption) {
        this.idx = idx;
        this.amount = amount;
        this.state = state;
        this.createdAt = createdAt;
        this.paymentOption = paymentOption;
    }

    public static PaymentDTO fromEntity(PaymentVO vo) {
        return PaymentDTO.builder()
                .amount(vo.getAmount())
                .paymentOption(PaymentOptionDTO.fromEntity(vo.getPaymentOption()))
                .state(vo.getState())
                .createdAt(vo.getCreatedAt())
                .idx(vo.getIdx())
                .build();
    }


}

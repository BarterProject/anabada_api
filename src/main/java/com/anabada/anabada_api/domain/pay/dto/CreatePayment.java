package com.anabada.anabada_api.domain.pay.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePayment {

    @Getter
    @Builder
    public static class Request{

        Long amount;
        Long optionIdx;

    }
}

package com.anabada.anabada_api.domain.delivery.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterTracking {

    @Builder
    @Getter
    public static class Request{
        String trackingNumber;

        Long deliveryCompanyIdx;
    }

}

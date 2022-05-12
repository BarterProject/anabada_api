package com.anabada.anabada_api.domain.delivery.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterTracking {

    @Builder
    @Getter
    public static class Request{
        String trackingNumber;

        Long deliveryCompanyIdx;
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        String message;
    }


}

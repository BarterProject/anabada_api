package com.anabada.anabada_api.domain.delivery.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CreateDelivery {


    @Builder
    @Getter
    public static class Request{

        @NotNull
        String phone;
        @NotNull
        String receiverName;
        @NotNull
        String address;
        @NotNull
        boolean clauseAgree;

    }
}

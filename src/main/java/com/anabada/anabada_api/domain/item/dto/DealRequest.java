package com.anabada.anabada_api.domain.item.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class DealRequest {


    @Getter
    @Builder
    public static class Request{

        @NotNull
        Long requestItemIdx;
        @NotNull
        Long responseItemIdx;
    }


    public static class Response{

    }


}

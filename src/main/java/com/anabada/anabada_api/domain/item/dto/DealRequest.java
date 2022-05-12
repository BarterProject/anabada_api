package com.anabada.anabada_api.domain.item.dto;


import lombok.AllArgsConstructor;
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


    @Getter
    @AllArgsConstructor
    public static class Response{
        Long idx;

    }


}

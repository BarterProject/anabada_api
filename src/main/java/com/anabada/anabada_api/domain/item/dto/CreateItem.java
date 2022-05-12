package com.anabada.anabada_api.domain.item.dto;

import com.anabada.anabada_api.domain.pay.dto.CreatePayment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateItem {

    @Getter
    @Builder
    public static class Request{

        CreatePayment.Request payment;
        Long categoryIdx;

        String name;
        String description;
        boolean clause;

        public boolean validate(){
            return payment != null && categoryIdx != null && name != null && description != null;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        Long idx;
    }
}

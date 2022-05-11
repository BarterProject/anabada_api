package com.anabada.anabada_api.domain.etc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateReport {

    @Getter
    @Builder
    public static class Request{

        String title;
        String content;

    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        Long idx;
    }
}

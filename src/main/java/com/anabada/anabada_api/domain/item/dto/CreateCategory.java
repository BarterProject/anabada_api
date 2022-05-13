package com.anabada.anabada_api.domain.item.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCategory {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "name이 입력되지 않았습니다.")
        String name;

    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Response {
        String name;
    }
}

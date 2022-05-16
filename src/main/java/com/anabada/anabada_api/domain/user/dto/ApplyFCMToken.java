package com.anabada.anabada_api.domain.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyFCMToken {

//    @Builder
    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotBlank(message = "토큰이 입력되지 않았습니다.")
        String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Response {
        Long idx;
    }

}

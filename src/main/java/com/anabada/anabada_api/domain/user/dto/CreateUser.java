package com.anabada.anabada_api.domain.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUser {

    @Builder
    @Getter
    public static class Request {
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        String email;

        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        String password;

        @NotBlank(message = "전화번호가 입력되지 않았습니다.")
        String phone;

        @NotBlank(message = "기본주소가 입력되지 않았습니다.")
        String address;

        @NotBlank(message = "계좌번호가 입력되지 않았습니다.")
        String bankAccount;

        @NotBlank(message = "은행명이 입력되지 않았습니다.")
        String bankKind;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Response {
        Long idx;
    }

}

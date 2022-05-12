package com.anabada.anabada_api.domain.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUser {

    @Builder
    @Getter
    public static class Request {
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        @Email
        String email;

        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상 이어야 합니다.")
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

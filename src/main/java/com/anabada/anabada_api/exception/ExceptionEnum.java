package com.anabada.anabada_api.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "B0001"),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "N0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "A0001"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S0001"),

    RUNTIME_EXCEPTION_MEMBER_DUPLICATED(HttpStatus.BAD_REQUEST, "B0002", "member duplicated"),
    RUNTIME_EXCEPTION_INVALID_DOMAIN(HttpStatus.BAD_REQUEST, "B0003", "email domain is not valid"),
    RUNTIME_EXCEPTION_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "B0004", "지원하지 않는 입력방식입니다."),
    RUNTIME_EXCEPTION_NOT_ACTIVATED(HttpStatus.BAD_REQUEST, "B0005", "활성화 되지 않았습니다."),
    RUNTIME_EXCEPTION_VALID_ERROR(HttpStatus.BAD_REQUEST, "B0006", "필수 파라미터가 없거나 형식이 잘못되었습니다.");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
}

package com.anabada.anabada_api.dto;

import lombok.Getter;

@Getter
public class MessageDTO {

    String message;

    public MessageDTO(String message) {
        this.message = message;
    }
}

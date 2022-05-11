package com.anabada.anabada_api.domain.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateMessage {

    @Builder
    @Getter
    public static class Request{
        String content;
        String roomName;
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        Long idx;
    }
}

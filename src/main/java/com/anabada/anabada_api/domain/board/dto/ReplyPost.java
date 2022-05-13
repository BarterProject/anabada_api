package com.anabada.anabada_api.domain.board.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class ReplyPost {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        @NotNull
        String content;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Repsonse{
        Long idx;
    }



}

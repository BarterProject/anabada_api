package com.anabada.anabada_api.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CreatePost {


    @Builder
    @Getter
    public static class Request{
        @NotBlank
        String title;
        @NotBlank
        String content;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Repsonse{
        Long idx;
    }



}

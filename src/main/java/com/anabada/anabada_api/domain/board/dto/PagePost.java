package com.anabada.anabada_api.domain.board.dto;

import com.anabada.anabada_api.domain.user.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagePost {

    List<PostDTO> posts = new ArrayList<>();

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("current_page")
    int currentPage;

    @Builder
    public PagePost(List<PostDTO> posts, int totalPage, int currentPage) {
        this.posts = posts;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }
}
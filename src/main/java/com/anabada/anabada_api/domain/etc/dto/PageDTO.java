package com.anabada.anabada_api.domain.etc.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageDTO<T> {
    List<T> contents = new ArrayList<>();

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("current_page")
    int currentPage;

    @Builder
    public PageDTO(List<T> contents, int totalPage, int currentPage) {
        this.contents = contents;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }
}

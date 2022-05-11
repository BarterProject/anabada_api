package com.anabada.anabada_api.domain.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageItemDTO {

    List<ItemDTO> items = new ArrayList<>();

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("current_page")
    int currentPage;

    @Builder

    public PageItemDTO(List<ItemDTO> items, int totalPage, int currentPage) {
        this.items = items;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }
}
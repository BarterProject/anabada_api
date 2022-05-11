package com.anabada.anabada_api.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageUserDTO {

    List<UserDTO> users = new ArrayList<>();

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("current_page")
    int currentPage;

    @Builder
    public PageUserDTO(List<UserDTO> users, int totalPage, int currentPage) {
        this.users = users;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }
}
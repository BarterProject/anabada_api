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
public class PageReportDTO {
    List<ReportDTO> reports = new ArrayList<>();

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("current_page")
    int currentPage;

    @Builder
    public PageReportDTO(List<ReportDTO> reports, int totalPage, int currentPage) {
        this.reports = reports;
        this.totalPage = totalPage;
        this.currentPage = currentPage;

    }
}

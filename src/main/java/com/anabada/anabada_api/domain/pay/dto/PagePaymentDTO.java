package com.anabada.anabada_api.domain.pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagePaymentDTO {

    List<PaymentDTO> payments = new ArrayList<>();

    @JsonProperty("total_page")
    int totalPage;

    @JsonProperty("current_page")
    int currentPage;


    @Builder
    public PagePaymentDTO(List<PaymentDTO> payments, int totalPage, int currentPage) {
        this.payments = payments;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
    }
}
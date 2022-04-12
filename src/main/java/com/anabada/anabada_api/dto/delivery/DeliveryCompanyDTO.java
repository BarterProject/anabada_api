package com.anabada.anabada_api.dto.delivery;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryCompanyDTO {
    private Long idx;

    private String name;

    private int code;

    @Builder
    public DeliveryCompanyDTO(Long idx,String name,int code){
        this.idx=idx;
        this.code=code;
        this.name=name;
    }
}

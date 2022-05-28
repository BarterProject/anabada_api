package com.anabada.anabada_api.domain.delivery.dto;


import com.anabada.anabada_api.domain.delivery.entity.DeliveryCompanyVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryCompanyDTO {
    private Long idx;

    private String name;

    private String code;

    @Builder
    public DeliveryCompanyDTO(Long idx,String name,String code){
        this.idx=idx;
        this.code=code;
        this.name=name;
    }

    public static DeliveryCompanyDTO fromEntity(DeliveryCompanyVO vo) {
        return DeliveryCompanyDTO.builder()
                .idx(vo.getIdx())
                .code(vo.getCode())
                .name(vo.getName())
                .build();
    }

}

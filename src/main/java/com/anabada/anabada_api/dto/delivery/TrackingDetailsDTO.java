package com.anabada.anabada_api.dto.delivery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrackingDetailsDTO {

    private String kind;
    //진행 상태

    private String timeString;
    //진행 시간

    private String where;
    //배송 진행 위치 지점

}

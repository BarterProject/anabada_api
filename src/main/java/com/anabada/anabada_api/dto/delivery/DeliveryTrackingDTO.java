package com.anabada.anabada_api.dto.delivery;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryTrackingDTO {

    private String itemName;

    private String invoiceNo;
    //운송장 번호

    private String level;
    //진행단계 [level 1: 배송준비중, 2: 집화완료, 3: 배송중, 4: 지점 도착, 5: 배송출발, 6:배송 완료]

    private String result;
    //조회 결과

    List<TrackingDetailsDTO>trackingDetails=new ArrayList<>();



}

package com.anabada.anabada_api.dto.delivery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterTrackingDTO {

    String trackingNumber;

    Long deliveryCompanyIdx;

}

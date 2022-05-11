package com.anabada.anabada_api.domain.delivery.dto;

import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.etc.dto.ValidationGroups;
import com.anabada.anabada_api.domain.item.dto.ItemDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryDTO {
    private Long idx;

    private int state;

    @NotBlank(groups = {ValidationGroups.deliveryRequestGroup.class}, message = "연락처가 입력되지않았습니다.")
    private String phone;

    @NotBlank(groups = {ValidationGroups.deliveryRequestGroup.class}, message = "수령자 이름이 입력되지않았습니다.")
    private String receiverName;

    @NotBlank(groups = {ValidationGroups.deliveryRequestGroup.class}, message = "주소가 입력되지않았습니다.")
    private String address;

    private boolean clauseAgree;

    private String trackingNumber;

    private LocalDateTime createdAt;

    private LocalDateTime dueAt;


    ItemDTO item;

    DeliveryCompanyDTO company;

    @Builder
    public DeliveryDTO(Long idx, String address, int state, LocalDateTime createdAt, LocalDateTime dueAt, String phone, String receiverName, boolean clauseAgree, String trackingNumber, ItemDTO item, DeliveryCompanyDTO company) {
        this.idx = idx;
        this.state = state;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
        this.phone = phone;
        this.address = address;
        this.receiverName = receiverName;
        this.clauseAgree = clauseAgree;
        this.trackingNumber = trackingNumber;
        this.item = item;
        this.company = company;
    }

    public static DeliveryDTO fromEntity(DeliveryVO vo) {
        return DeliveryDTO.builder()
                .idx(vo.getIdx())
                .state(vo.getState())
                .createdAt(vo.getCreatedAt())
                .dueAt(vo.getDueAt())
                .phone(vo.getPhone())
                .address(vo.getAddress())
                .receiverName(vo.getReceiverName())
                .trackingNumber(vo.getTrackingNumber())
                .build();
//                .company(DeliveryCompanyDTO.fromEntity(vo.getDeliveryCompany())).build();
    }


}

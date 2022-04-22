package com.anabada.anabada_api.dto.item;

import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.dto.delivery.DeliveryDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.dto.user.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ItemDTO {

    private Long idx;

    @NotBlank(groups = {ValidationGroups.itemSaveGroup.class}, message = "이름이 입력되지 않았습니다.")
    private String name;

    @NotBlank(groups = {ValidationGroups.itemSaveGroup.class}, message = "설명이 입력되지 않았습니다.")
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime endAt;

    private Long deposit;

    @NotNull(groups = {ValidationGroups.itemSaveGroup.class}, message = "약관동의 값이 입력되지 않았습니다.")
    private boolean clause_agree;

    @NotNull(groups = {ValidationGroups.itemSaveGroup.class}, message = "결제 정보가 입력되지 않았습니다.")
    private PaymentDTO payment;

    @NotNull(groups = {ValidationGroups.itemSaveGroup.class}, message = "카테고리 정보가 입력되지 않았습니다.")
    private ItemCategoryDTO itemCategory;

    private List<ItemImageDTO> images;

    private UserDTO registrant;

    private UserDTO owner;

    private Long state;

    private DeliveryDTO delivery;

    @Builder
    public ItemDTO(Long idx, String name, String description, LocalDateTime createdAt, LocalDateTime endAt, Long deposit, boolean clause_agree, PaymentDTO payment, ItemCategoryDTO itemCategory, UserDTO registrant, UserDTO owner, Long state, List<ItemImageDTO> images,DeliveryDTO delivery) {
        this.idx = idx;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.endAt = endAt;
        this.deposit = deposit;
        this.payment = payment;
        this.state = state;
        this.clause_agree = clause_agree;
        this.registrant = registrant;
        this.itemCategory = itemCategory;
        this.owner = owner;
        this.images = images;
        this.delivery=delivery;
    }

    public ItemVO toEntity(PaymentOptionVO paymentOption, DeliveryVO delivery) {
        return ItemVO.builder()
                .name(this.name)
                .description(this.description)
                .clauseAgree(this.clause_agree)
                .deposit(this.deposit)
                .payment(payment.toEntity(paymentOption))
                .state(this.state)
                .delivery(delivery)
                .build();
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }
}


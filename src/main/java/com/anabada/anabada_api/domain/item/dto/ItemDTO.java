package com.anabada.anabada_api.domain.item.dto;

import com.anabada.anabada_api.domain.delivery.dto.DeliveryDTO;
import com.anabada.anabada_api.domain.etc.dto.ValidationGroups;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.pay.dto.PaymentDTO;
import com.anabada.anabada_api.domain.user.dto.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private int state;

    private DeliveryDTO delivery;

    @Builder
    public ItemDTO(Long idx, String name, String description, LocalDateTime createdAt, LocalDateTime endAt, Long deposit, boolean clause_agree, PaymentDTO payment, ItemCategoryDTO itemCategory, UserDTO registrant, UserDTO owner, int state, List<ItemImageDTO> images, DeliveryDTO delivery) {
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
        this.delivery = delivery;
    }

    public static ItemDTO listFromEntity(ItemVO vo) {

        return ItemDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .description(vo.getDescription())
                .endAt(vo.getEndAt())
                .state(vo.getState())
                .deposit(vo.getDeposit())
                .itemCategory(ItemCategoryDTO.fromEntity(vo.getItemCategory()))
                .createdAt(vo.getCreatedAt())
                .description(vo.getDescription())
                .owner(UserDTO.simpleFromEntity(vo.getOwner()))
                .clause_agree(vo.isClauseAgree())
                .images(
                        vo.getImages().stream().map(ItemImageDTO::fromEntity).collect(Collectors.toList())
                )
                .build();
    }

    public static ItemDTO fromEntityByOwner(ItemVO vo) {
        return ItemDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .description(vo.getDescription())
                .endAt(vo.getEndAt())
                .state(vo.getState())
                .itemCategory(ItemCategoryDTO.fromEntity(vo.getItemCategory()))
                .createdAt(vo.getCreatedAt())
                .deposit(vo.getDeposit())
                .description(vo.getDescription())
                .clause_agree(vo.isClauseAgree())
                .images(
                        vo.getImages().stream().map(ItemImageDTO::fromEntity).collect(Collectors.toList())
                )

                .owner(UserDTO.simpleFromEntity(vo.getOwner()))
                .registrant(UserDTO.simpleFromEntity(vo.getRegistrant()))
                .delivery(vo.getDelivery() != null ? DeliveryDTO.fromEntity(vo.getDelivery()) : null)
                .build();
    }

    public static ItemDTO fromEntityByRegistrant(ItemVO vo) {
        return ItemDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .description(vo.getDescription())
                .endAt(vo.getEndAt())
                .state(vo.getState())
                .itemCategory(ItemCategoryDTO.fromEntity(vo.getItemCategory()))
                .createdAt(vo.getCreatedAt())
                .deposit(vo.getDeposit())
                .description(vo.getDescription())
                .clause_agree(vo.isClauseAgree())
                .images(
                        vo.getImages().stream().map(ItemImageDTO::fromEntity).collect(Collectors.toList())
                )

                .owner(UserDTO.simpleFromEntity(vo.getOwner()))
                .payment(PaymentDTO.fromEntity(vo.getPayment()))
                .build();
    }

    public static ItemDTO fromEntityByHistory(ItemVO vo) {
        return ItemDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .images(
                        vo.getImages().stream().map(ItemImageDTO::fromEntity).limit(1).collect(Collectors.toList())
                )
                .deposit(vo.getDeposit())
                .build();
    }

    public static ItemDTO allFromEntity(ItemVO vo) {
        return ItemDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .description(vo.getDescription())
                .endAt(vo.getEndAt())
                .state(vo.getState())
                .itemCategory(ItemCategoryDTO.fromEntity(vo.getItemCategory()))
                .createdAt(vo.getCreatedAt())
                .deposit(vo.getDeposit())
                .description(vo.getDescription())
                .images(
                        vo.getImages().stream().map(ItemImageDTO::fromEntity).collect(Collectors.toList())
                )
                .registrant(UserDTO.simpleFromEntity(vo.getRegistrant()))
                .owner(UserDTO.simpleFromEntity(vo.getOwner()))
//                .payment(PaymentDTO.fromEntity(vo.getPayment()))
                .build();
    }

    public static ItemDTO onlyIdxFromEntity(ItemVO vo) {
        return ItemDTO.builder()
                .idx(vo.getIdx())
                .build();
    }


}


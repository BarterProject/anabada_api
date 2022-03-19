package com.anabada.anabada_api.dto.item;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.dto.user.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ItemDTO {

        private Long idx;

        private String name;

        private String  description;

        private LocalDateTime createdAt;

        private LocalDateTime endAt;

        private Long deposit;

        private boolean clause_agree;

        private PaymentDTO payment;

        private ItemCategoryDTO itemCategory;

        private UserDTO user;

        private Long state;


    @Builder
        public ItemDTO(Long idx,String name,String description,LocalDateTime createdAt,LocalDateTime deletedAt,LocalDateTime endAt,Long deposit,boolean clause_agree,PaymentDTO payment,ItemCategoryDTO itemCategory,UserDTO user,Long state){
            this.idx=idx;
            this.name=name;
            this.description=description;
            this.createdAt=createdAt;
            this.endAt=endAt;
            this.deposit=deposit;
            this.payment=payment;
            this.state=state;
            this.clause_agree=clause_agree;
            this.user=user;
        }

        public ItemVO toEntity(PaymentOptionVO paymentOption){
            return ItemVO.builder()
                    .name(this.name)
                    .description(this.description)
                    .clauseAgree(this.clause_agree)
                    .deposit(this.deposit)
                    .payment(payment.toEntity(paymentOption))
                    .state(this.state)
                    .build();
        }
}


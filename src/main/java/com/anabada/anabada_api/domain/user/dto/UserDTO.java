package com.anabada.anabada_api.domain.user.dto;

import com.anabada.anabada_api.domain.user.entity.UserVO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserDTO {

    private Long idx;

    String email;
    String password;
    String phone;
    String address;
    String bankAccount;
    String bankKind;

    LocalDateTime createdAt;

    String auth;

    public static UserDTO myInfoFromEntity(UserVO user){
        return UserDTO.builder()
                .idx(user.getIdx())
                .email(user.getEmail())
                .password("*")
                .address(user.getAddress())
                .bankAccount(user.getBankAccount())
                .bankKind(user.getBankKind())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .auth(user.getAuth().getName())
                .build();
    }

    public static UserDTO allInfoFromEntity(UserVO user){
        return UserDTO.builder()
                .idx(user.getIdx())
                .email(user.getEmail())
                .password("*")
                .address(user.getAddress())
                .phone(user.getPhone())
                .bankAccount(user.getBankAccount())
                .bankKind(user.getBankKind())
                .createdAt(user.getCreatedAt())
                .auth(user.getAuth().getName())
                .build();
    }

    public static UserDTO simpleFromEntity(UserVO user){
        return UserDTO.builder()
                .idx(user.getIdx())
                .email(user.getEmail())
                .build();
    }



}

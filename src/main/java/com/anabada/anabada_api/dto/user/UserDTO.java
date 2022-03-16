package com.anabada.anabada_api.dto.user;

import com.anabada.anabada_api.dto.ValidationGroups;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

    private Long idx;

    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "아이디가 입력되지 않았습니다.")
    String email;

    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "비밀번호가 입력되지 않았습니다.")
    String password;

    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "전화번호가 입력되지 않았습니다.")
    String phone;

    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "기본주소가 입력되지 않았습니다.")
    String address;

    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "계좌번호가 입력되지 않았습니다.")
    String bankAccount;

    @NotBlank(groups = {ValidationGroups.userSignUpGroup.class}, message = "은행명이 입력되지 않았습니다.")
    String bankKind;

    LocalDateTime createdAt;

    String oauth;

    boolean activated;

    String auth;

    @Builder
    public UserDTO(Long idx, String email, String password, String phone, String address, String bankAccount, String bankKind, LocalDateTime createdAt, String oauth, boolean activated, String auth) {
        this.idx = idx;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.bankAccount = bankAccount;
        this.bankKind = bankKind;
        this.createdAt = createdAt;
        this.oauth = oauth;
        this.activated = activated;
        this.auth = auth;
    }

    //TODO 엔티티들 구현 후 아래 구현
//    private List<NoticeVO> notices = new ArrayList<>();

//    private List<PostVO> posts = new ArrayList<>();

//    private UserImageVO userImage;

//    private List<ReportVO> reports = new ArrayList<>();

}

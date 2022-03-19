package com.anabada.anabada_api.domain.user;


import com.anabada.anabada_api.domain.NoticeVO;
import com.anabada.anabada_api.domain.board.PostVO;
import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.dto.user.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "USER_TB")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false)
    private Long idx;

    @Column(name = "email", updatable = false, nullable = false, length = 100)
    String email;

    @Column(name = "password", updatable = true, nullable = false, length = 255)
    String password;

    @Column(name = "phone", updatable = true, nullable = true, length = 45)
    String phone;

    @Column(name = "address", updatable = true, nullable = true, length = 200)
    String address;

    @Column(name = "bank_account", updatable = true, nullable = true, length = 45)
    String bankAccount;

    @Column(name = "bank_kind", updatable = true, nullable = true, length = 45)
    String bankKind;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @Column(name = "oauth", updatable = false, nullable = true, length = 45)
    String oauth;

    @Column(name = "activated", updatable = true, nullable = true)
    boolean activated;

    @OneToOne(targetEntity = AuthVO.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_name_fk", nullable = false, updatable = true)
    AuthVO auth;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeVO> notices = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostVO> posts = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_image_idx_fk")
    private UserImageVO userImage;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportVO> reports = new ArrayList<>();

    @Builder
    public UserVO(Long idx, String email, String password, String phone, String address, String bankAccount, String bankKind, LocalDateTime createdAt, String oauth, boolean activated, AuthVO auth) {
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

    public UserDTO dto(){
        return UserDTO.builder()
                .email(email)
                .password("*")
                .phone(phone)
                .address(address)
                .bankAccount(bankAccount)
                .bankKind(bankKind)
                .activated(activated)
                .oauth(oauth)
                .auth(auth.getName())
                .createdAt(createdAt)
                .build();
    }

    public void setUserImage(UserImageVO userImage) {
        this.userImage = userImage;
    }
}

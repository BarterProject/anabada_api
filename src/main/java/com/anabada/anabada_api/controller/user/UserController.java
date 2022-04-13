package com.anabada.anabada_api.controller.user;


import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.user.UserDTO;
import com.anabada.anabada_api.service.user.UserFindService;
import com.anabada.anabada_api.service.user.UserUpdateService;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.message.AuthException;

@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserUpdateService userUpdateService;

    @Autowired
    UserFindService userFindService;


    /**
     * 유저 회원가입
     *
     * @param requestUserDTO email: 사용자 ID
     *                       password: 비밀번호
     *                       phone: 전화번호
     *                       address: 집 주소
     *                       bankAccount: 은행 계좌
     *                       bankKind: 은행 종류
     * @return UserDTO: 회원가입된 유저정보
     * @throws DuplicateMemberException UserVO email 중복 오류
     * @throws NotFoundException        올바르지 않은 권한유형 오류 (서버오류, 사용자로부터 입력받는 값 x)
     */
    @PostMapping(path = "/user")
    public ResponseEntity<UserDTO> getSignUp (@RequestBody @Validated(ValidationGroups.userSignUpGroup.class) UserDTO requestUserDTO) throws DuplicateMemberException, NotFoundException {

        UserDTO user = userUpdateService.signUp(requestUserDTO);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(path = "/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDTO> getUserInfo() throws AuthException {

        UserVO uservo = userFindService.getMyUserWithAuthorities();

        return new ResponseEntity<>(uservo.dto(true), HttpStatus.OK);
    }


}

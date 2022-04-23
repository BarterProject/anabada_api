package com.anabada.anabada_api.controller.user;


import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.user.PageUserDTO;
import com.anabada.anabada_api.dto.user.UserDTO;
import com.anabada.anabada_api.service.user.UserFindService;
import com.anabada.anabada_api.service.user.UserUpdateService;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserDTO> getSignUp(@RequestBody @Validated(ValidationGroups.userSignUpGroup.class) UserDTO requestUserDTO) throws DuplicateMemberException, NotFoundException {

        UserDTO user = userUpdateService.signUp(requestUserDTO);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(path = "/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDTO> getUserInfo() throws AuthException {

        UserVO uservo = userFindService.getMyUserWithAuthorities();

        return new ResponseEntity<>(uservo.dto(true), HttpStatus.OK);
    }

    /* --- 관리자기능 --- */

    @GetMapping("/admin/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<PageUserDTO> getUserList(
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageUserDTO page = userFindService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/admin/user/{user-idx}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUserByIdx(
            @PathVariable(value = "user-idx")Long userIdx
    ) throws NotFoundException {
        UserDTO user = userFindService.findByIdxDTO(userIdx);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/admin/user/{user-idx}/deactivation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deactivateUser(
            @PathVariable(value = "user-idx")Long userIdx
    ) throws NotFoundException {
        userUpdateService.activateUser(userIdx, false);
        return new ResponseEntity<>(new MessageDTO("user deactivated"), HttpStatus.OK);
    }

    @PutMapping("/admin/user/{user-idx}/activation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> activateUser(
            @PathVariable(value = "user-idx")Long userIdx
    ) throws NotFoundException {
        userUpdateService.activateUser(userIdx, true);
        return new ResponseEntity<>(new MessageDTO("user activated"), HttpStatus.OK);
    }


}

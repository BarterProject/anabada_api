package com.anabada.anabada_api.domain.user.controller;


import com.anabada.anabada_api.domain.user.dto.CreateUser;
import com.anabada.anabada_api.domain.user.dto.PageUserDTO;
import com.anabada.anabada_api.domain.user.dto.UserDTO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.domain.user.service.UserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserUpdateService userUpdateService;

    @Autowired
    UserFindService userFindService;

    @PostMapping(path = "/user")
    public ResponseEntity<CreateUser.Response> getSignUp(
            @RequestBody @Validated CreateUser.Request request){

        Long idx = userUpdateService.signUp(request);

        return new ResponseEntity<>(new CreateUser.Response(idx), HttpStatus.CREATED);
    }

    @GetMapping(path = "/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDTO> getUserInfo() {

        UserVO user = userFindService.getMyUserWithAuthorities();

        return new ResponseEntity<>(UserDTO.myInfoFromEntity(user), HttpStatus.OK);
    }

    /* --- 관리자기능 --- */

    @GetMapping("/admin/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<PageUserDTO> getUserList(
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<UserVO> page = userFindService.findAll(pageable);
        List<UserDTO> dtos = page.getContent().stream().map(UserDTO::allInfoFromEntity).collect(Collectors.toList());

        PageUserDTO pageDTO = PageUserDTO.builder()
                .users(dtos)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @GetMapping("/admin/user/{user-idx}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUserByIdx(
            @PathVariable(value = "user-idx") Long userIdx
    ) {
        UserVO user = userFindService.findByIdxDTO(userIdx);
        UserDTO userDto = UserDTO.allInfoFromEntity(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/admin/user/{user-idx}/deactivation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deactivateUser(
            @PathVariable(value = "user-idx") Long userIdx
    ) {
        userUpdateService.activateUser(userIdx, false);

        return new ResponseEntity<>(new MessageDTO("user deactivated"), HttpStatus.OK);
    }

    @PutMapping("/admin/user/{user-idx}/activation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> activateUser(
            @PathVariable(value = "user-idx") Long userIdx
    ) {
        userUpdateService.activateUser(userIdx, true);

        return new ResponseEntity<>(new MessageDTO("user activated"), HttpStatus.OK);
    }


}

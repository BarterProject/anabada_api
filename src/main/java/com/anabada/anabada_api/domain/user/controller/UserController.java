package com.anabada.anabada_api.domain.user.controller;


import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import com.anabada.anabada_api.domain.user.dto.ApplyFCMToken;
import com.anabada.anabada_api.domain.user.dto.CreateUser;
import com.anabada.anabada_api.domain.user.dto.PageUserDTO;
import com.anabada.anabada_api.domain.user.dto.UserDTO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.domain.user.service.UserImageService;
import com.anabada.anabada_api.domain.user.service.UserUpdateService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.anabada.anabada_api.firebase.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserUpdateService userUpdateService;

    @Autowired
    FCMService fcmService;

    @Autowired
    UserFindService userFindService;

    @Autowired
    UserImageService userImageService;

    @PostMapping(path = "/v2/user")
    public ResponseEntity<CreateUser.Response> getSignUp(
            @RequestBody @Validated CreateUser.Request request) {

        Long idx = userUpdateService.signUp(request);

        return new ResponseEntity<>(new CreateUser.Response(idx), HttpStatus.CREATED);
    }

    @GetMapping(path = "/v2/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<UserDTO> getUserInfo() {

        UserVO user = userFindService.getMyUserWithAuthorities();
//        fcmService.sendMessageTest();
//        fcmService.sendNotificationTest();

        return new ResponseEntity<>(UserDTO.myInfoFromEntity(user), HttpStatus.OK);
    }

    @PutMapping(value = "/v2/user/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<MessageDTO> updateUserImage(
            @RequestPart(value = "img", required = true) MultipartFile mf
    ) {
        userUpdateService.saveUserImage(mf);
        return new ResponseEntity<>(new MessageDTO("success"), HttpStatus.OK);
    }

    @GetMapping(value = "/v2/user/image/{image-name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getUserImage(
            @PathVariable(value = "image-name") String userImage
    ) {
        byte[] image = userImageService.getByName(userImage);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @PutMapping("/v2/user/fcm/token")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<MessageDTO> updateFCMToken(
            @RequestBody @Validated ApplyFCMToken.Request request
    ) {
        userUpdateService.updateFCMToken(request);
        return new ResponseEntity<>(new MessageDTO("token updated"), HttpStatus.OK);
    }

    @DeleteMapping("/v2/user/fcm/token")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<MessageDTO> deleteFCMToken(
    ) {
        userUpdateService.deleteFCMToken();
        return new ResponseEntity<>(new MessageDTO("token deleted"), HttpStatus.OK);
    }

    /* --- 관리자기능 --- */

    @GetMapping("/v2/admin/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<PageUserDTO> getUserList(
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "mode", required = false, defaultValue = "all") String mode,
            @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        Page<UserVO> page;

        if (mode.equals("all")) {
            page = userFindService.findAll(pageable);
        } else if (mode.equals("email")) {
            page = userFindService.findByEmail(query, pageable);
        } else {
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }

        List<UserDTO> dtos = page.getContent().stream().map(UserDTO::allInfoFromEntity).collect(Collectors.toList());

        PageUserDTO pageDTO = PageUserDTO.builder()
                .users(dtos)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @GetMapping("/v2/admin/user/{user-idx}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getUserByIdx(
            @PathVariable(value = "user-idx") Long userIdx
    ) {
        UserVO user = userFindService.findByIdxDTO(userIdx);
        UserDTO userDto = UserDTO.allInfoFromEntity(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/v2/admin/user/{user-idx}/deactivation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deactivateUser(
            @PathVariable(value = "user-idx") Long userIdx
    ) {
        userUpdateService.activateUser(userIdx, false);

        return new ResponseEntity<>(new MessageDTO("user deactivated"), HttpStatus.OK);
    }

    @PutMapping("/v2/admin/user/{user-idx}/activation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> activateUser(
            @PathVariable(value = "user-idx") Long userIdx
    ) {
        userUpdateService.activateUser(userIdx, true);

        return new ResponseEntity<>(new MessageDTO("user activated"), HttpStatus.OK);
    }


}

package com.anabada.anabada_api.controller.user;


import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.user.UserDTO;
import com.anabada.anabada_api.service.user.UserUpdateService;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserUpdateService userUpdateService;


    @PostMapping(path = "/user")
    public ResponseEntity<UserDTO> getSignUp (@RequestBody @Validated(ValidationGroups.userSignUpGroup.class) UserDTO requestUserDTO) throws DuplicateMemberException, NotFoundException {

        UserDTO user = userUpdateService.signUp(requestUserDTO);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }



}

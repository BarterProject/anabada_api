package com.anabada.anabada_api.controller.user;


import com.anabada.anabada_api.dto.user.NoticeDTO;
import com.anabada.anabada_api.service.user.NoticeFindService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NoticeController {


    NoticeFindService noticeFindService;

    public NoticeController(NoticeFindService noticeFindService) {
        this.noticeFindService = noticeFindService;
    }

    @GetMapping("/users/notices")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<NoticeDTO>> getMyNotices() throws AuthException {
        List<NoticeDTO> myNotices = noticeFindService.getMyNotices();
        return new ResponseEntity<>(myNotices, HttpStatus.OK);
    }
}

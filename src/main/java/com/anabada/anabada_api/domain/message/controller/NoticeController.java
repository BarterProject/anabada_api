package com.anabada.anabada_api.domain.message.controller;


import com.anabada.anabada_api.domain.message.dto.NoticeDTO;
import com.anabada.anabada_api.domain.message.entity.NoticeVO;
import com.anabada.anabada_api.domain.message.service.NoticeFindService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class NoticeController {


    NoticeFindService noticeFindService;

    public NoticeController(NoticeFindService noticeFindService) {
        this.noticeFindService = noticeFindService;
    }

    @GetMapping("/v2/users/notices")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<NoticeDTO>> getMyNotices() {
        List<NoticeVO> list = noticeFindService.getMyNotices();
        List<NoticeDTO> dtos = list.stream().map(NoticeDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}

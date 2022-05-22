package com.anabada.anabada_api.domain.message.controller;


import com.anabada.anabada_api.domain.etc.dto.PageDTO;
import com.anabada.anabada_api.domain.message.dto.MessageEntityDTO;
import com.anabada.anabada_api.domain.message.dto.NoticeDTO;
import com.anabada.anabada_api.domain.message.entity.NoticeVO;
import com.anabada.anabada_api.domain.message.service.NoticeFindService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PageDTO<NoticeDTO>> getMyNotices(
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<NoticeVO> page = noticeFindService.getMyNotices(pageable);
        List<NoticeDTO> dtos = page.getContent().stream().map(NoticeDTO::fromEntity).collect(Collectors.toList());

        PageDTO<NoticeDTO> noticeDTO = PageDTO.<NoticeDTO>builder()
                .contents(dtos)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(noticeDTO, HttpStatus.OK);
    }
}

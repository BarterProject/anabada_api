package com.anabada.anabada_api.domain.board.controller;

import com.anabada.anabada_api.domain.board.dto.CreatePost;
import com.anabada.anabada_api.domain.board.dto.PostDTO;
import com.anabada.anabada_api.domain.board.dto.ReplyPost;
import com.anabada.anabada_api.domain.board.entity.PostVO;
import com.anabada.anabada_api.domain.board.service.PostFindService;
import com.anabada.anabada_api.domain.board.service.PostUpdateService;
import com.anabada.anabada_api.domain.etc.dto.PageDTO;
import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class PostController {


    PostUpdateService postUpdateService;
    PostFindService postFindService;

    public PostController(PostUpdateService postUpdateService, PostFindService postFindService) {
        this.postUpdateService = postUpdateService;
        this.postFindService = postFindService;
    }

    @PostMapping("/v2/boards/{board-idx}/posts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<CreatePost.Repsonse> save(
            @PathVariable(name = "board-idx") Long boardIdx,
            @RequestBody @Validated CreatePost.Request request
    ) {
        Long idx = postUpdateService.save(boardIdx, request);
        return new ResponseEntity<>(new CreatePost.Repsonse(idx), HttpStatus.CREATED);
    }

    @GetMapping("/v2/boards/{board-idx}/posts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<PostDTO>> geyMyPosts(
            @PathVariable(name = "board-idx") Long boardIdx
    ) {
        List<PostVO> posts = postFindService.findMyPosts(boardIdx);
        List<PostDTO> dtos = posts.stream().map(PostDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }


    //admin functions

    @GetMapping("/v2/admin/boards/{board-idx}/posts")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<PageDTO<PostDTO>> getAllPosts(
            @PathVariable(name = "board-idx") Long boardIdx,
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PostVO> page = postFindService.findAll(boardIdx, pageable);
        List<PostDTO> dtos = page.getContent().stream().map(PostDTO::fromEntityWithUser).collect(Collectors.toList());

        PageDTO<PostDTO> pageDTO = PageDTO.<PostDTO>builder()
                .contents(dtos)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @PutMapping("/v2/admin/boards/posts/{post-idx}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> replyOnPost(
            @PathVariable(name = "post-idx") Long postIdx,
            @RequestBody @Validated ReplyPost.Request request
    ) {
        postUpdateService.replyOnPost(postIdx, request);
        return new ResponseEntity<>(new MessageDTO("reply success"), HttpStatus.OK);
    }
}

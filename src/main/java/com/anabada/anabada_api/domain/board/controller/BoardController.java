package com.anabada.anabada_api.domain.board.controller;

import com.anabada.anabada_api.domain.board.dto.BoardDTO;
import com.anabada.anabada_api.domain.board.entity.BoardVO;
import com.anabada.anabada_api.domain.board.service.BoardFindService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BoardController {


    @Autowired
    BoardFindService boardFindService;


    @GetMapping("/v2/boards")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<BoardDTO>> getAll(){
        List<BoardVO> boards = boardFindService.findAll();
        List<BoardDTO> dtos = boards.stream().map(BoardDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}

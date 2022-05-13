package com.anabada.anabada_api.domain.board.service;

import com.anabada.anabada_api.domain.board.entity.BoardVO;
import com.anabada.anabada_api.domain.board.repository.BoardRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardFindService {

    @Autowired
    BoardRepository boardRepository;


    @Transactional(readOnly = true)
    public List<BoardVO> findAll(){
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BoardVO findByIdx(Long idx){
        return boardRepository.findById(idx).orElseThrow(() -> new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION));
    }


}

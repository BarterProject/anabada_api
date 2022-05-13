
package com.anabada.anabada_api.domain.board.service;

import com.anabada.anabada_api.domain.board.entity.BoardVO;
import com.anabada.anabada_api.domain.board.entity.PostVO;
import com.anabada.anabada_api.domain.board.repository.PostRepository;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostFindService {

    PostRepository postRepository;

    UserFindService userFindService;
    BoardFindService boardFindService;

    public PostFindService(PostRepository postRepository, UserFindService userFindService, BoardFindService boardFindService) {
        this.postRepository = postRepository;
        this.userFindService = userFindService;
        this.boardFindService = boardFindService;
    }

    @Transactional(readOnly = true)
    public List<PostVO> findMyPosts(Long boardIdx){
        UserVO user = userFindService.getMyUserWithAuthorities();
        BoardVO board = boardFindService.findByIdx(boardIdx);

        return postRepository.findByUserAndBoard(user, board);
    }

    @Transactional(readOnly = true)
    public Page<PostVO> findAll(Long boardIdx, Pageable pageable){
        BoardVO board = boardFindService.findByIdx(boardIdx);

        return postRepository.findByBoard(board, pageable);
    }


    public PostVO findByIdx(Long postIdx) {
        return postRepository.findById(postIdx).orElseThrow(() -> new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION));
    }
}

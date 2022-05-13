
package com.anabada.anabada_api.domain.board.service;

import com.anabada.anabada_api.domain.board.dto.CreatePost;
import com.anabada.anabada_api.domain.board.dto.ReplyPost;
import com.anabada.anabada_api.domain.board.entity.BoardVO;
import com.anabada.anabada_api.domain.board.entity.PostVO;
import com.anabada.anabada_api.domain.board.repository.PostRepository;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostUpdateService {

    PostRepository postRepository;

    UserFindService userFindService;
    BoardFindService boardFindService;
    PostFindService postFindService;

    public PostUpdateService(PostRepository postRepository, UserFindService userFindService, BoardFindService boardFindService, PostFindService postFindService) {
        this.postRepository = postRepository;
        this.userFindService = userFindService;
        this.boardFindService = boardFindService;
        this.postFindService = postFindService;
    }

    @Transactional
    public Long save(Long boardIdx, CreatePost.Request request){

        UserVO user = userFindService.getMyUserWithAuthorities();
        BoardVO board = boardFindService.findByIdx(boardIdx);

        PostVO post = PostVO.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .board(board)
                .build();

        return postRepository.save(post).getIdx();


    }


    @Transactional
    public void replyOnPost(Long postIdx, ReplyPost.Request request) {
        PostVO post = postFindService.findByIdx(postIdx);
        post.setReply(request.getContent());
    }
}

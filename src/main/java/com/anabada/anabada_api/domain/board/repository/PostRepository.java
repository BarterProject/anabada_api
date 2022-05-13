
package com.anabada.anabada_api.domain.board.repository;

import com.anabada.anabada_api.domain.board.entity.BoardVO;
import com.anabada.anabada_api.domain.board.entity.PostVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostVO, Long> {


    List<PostVO> findByUserAndBoard(UserVO user, BoardVO board);

    @EntityGraph(attributePaths = {"user"})
    Page<PostVO> findByBoard(BoardVO board, Pageable pageable);
}
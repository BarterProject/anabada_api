
package com.anabada.anabada_api.domain.board.repository;

import com.anabada.anabada_api.domain.board.entity.BoardVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardVO, Long> {

}
package com.anabada.anabada_api.domain.message.repository;

import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;



@Repository
public interface MessageRepository extends JpaRepository<MessageVO,Long> {


    @EntityGraph(attributePaths = {"sender"})
    public Page<MessageVO> findByRoom(RoomVO room, Pageable pageable);
}

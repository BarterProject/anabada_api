package com.anabada.anabada_api.domain.message.repository;

import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<MessageVO,Long> {


    @EntityGraph(attributePaths = {"sender"})
    public List<MessageVO> findByRoom(RoomVO room);
}

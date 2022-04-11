package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.message.MessageVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<MessageVO,Long> {


    public List<MessageVO> findByRoom(RoomVO room);
}

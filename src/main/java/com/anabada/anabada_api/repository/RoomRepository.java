package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.message.RoomVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomVO,Long> {

}

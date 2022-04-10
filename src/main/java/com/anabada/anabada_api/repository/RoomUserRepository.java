package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.message.RoomUserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RoomUserRepository extends JpaRepository<RoomUserVO,Long>  {
}


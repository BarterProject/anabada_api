package com.anabada.anabada_api.repository;


import com.anabada.anabada_api.domain.message.RoomUserVO;
import com.anabada.anabada_api.domain.user.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomUserRepository extends JpaRepository<RoomUserVO,Long> {

    List<RoomUserVO> findAllByUser(UserVO user);


}


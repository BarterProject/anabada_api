package com.anabada.anabada_api.repository;


import com.anabada.anabada_api.domain.message.RoomUserMappingVO;
import com.anabada.anabada_api.domain.user.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomUserRepository extends JpaRepository<RoomUserMappingVO,Long> {

    List<RoomUserMappingVO> findAllByUser(UserVO user);


}


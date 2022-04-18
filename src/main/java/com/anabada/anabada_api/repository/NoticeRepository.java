package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.NoticeVO;
import com.anabada.anabada_api.domain.user.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoticeRepository extends JpaRepository<NoticeVO, Long> {

    public List<NoticeVO> getAllByUser(UserVO user);

}

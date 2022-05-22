package com.anabada.anabada_api.domain.message.repository;

import com.anabada.anabada_api.domain.message.entity.NoticeVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoticeRepository extends JpaRepository<NoticeVO, Long> {

    public Page<NoticeVO> getAllByUser(UserVO user, Pageable pageable);

}

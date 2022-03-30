package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.NoticeVO;
import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<ReportVO,Long> {
    public Page<ReportVO> findAll(Pageable pageable);
    public Page<ReportVO>findByItem(ItemVO item,Pageable pageable);
    public Page<ReportVO>findAllByUser(UserVO user,Pageable pageable);
}

package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.DealRequestVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DealRequestRepository extends JpaRepository<DealRequestVO, Long> {

}

package com.anabada.anabada_api.domain.delivery.repository;

import com.anabada.anabada_api.domain.delivery.entity.DeliveryCompanyVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompanyVO,Long> {

}

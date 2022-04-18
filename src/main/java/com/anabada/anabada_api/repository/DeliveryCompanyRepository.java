package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.delivery.DeliveryCompanyVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompanyVO,Long> {

}

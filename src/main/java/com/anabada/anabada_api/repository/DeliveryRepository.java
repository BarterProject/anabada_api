package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryVO,Long> {
    public boolean existsByState(Long state);
}

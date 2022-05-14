package com.anabada.anabada_api.domain.delivery.repository;

import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryVO, Long> {


    Optional<DeliveryVO> findByIdx(Long idx);

    Optional<DeliveryVO> findByItem(ItemVO item);

    DeliveryVO findByTrackingNumber(String trackingNumber);
}

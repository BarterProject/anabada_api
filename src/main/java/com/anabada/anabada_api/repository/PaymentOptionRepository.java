package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentOptionRepository extends JpaRepository<PaymentOptionVO, Long> {
    public Optional<PaymentOptionVO> findByName(String name);
}

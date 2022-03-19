package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentOptionRepository extends JpaRepository<PaymentOptionVO,Long > {
    public Optional<PaymentOptionVO> findByName(String name);
}

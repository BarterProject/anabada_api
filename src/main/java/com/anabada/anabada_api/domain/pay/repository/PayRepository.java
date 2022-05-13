package com.anabada.anabada_api.domain.pay.repository;

import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.pay.entity.PaymentVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayRepository extends JpaRepository<PaymentVO,Long> {
   public Page<PaymentVO> findAll(Pageable pageable);

   public Optional<PaymentVO> findByItem(ItemVO item);

   // public Page<PaymentVO>findAllByUser(UserVO user,Pageable pageable);


}

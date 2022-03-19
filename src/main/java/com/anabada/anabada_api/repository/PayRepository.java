package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;
import com.anabada.anabada_api.domain.user.UserVO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PayRepository extends JpaRepository<PaymentVO,Long> {
   public Page<PaymentVO> findAll(Pageable pageable);

  // public Page<PaymentVO>findAllByUser(UserVO user,Pageable pageable);


}

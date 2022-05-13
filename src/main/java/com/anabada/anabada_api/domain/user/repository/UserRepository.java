
package com.anabada.anabada_api.domain.user.repository;

import com.anabada.anabada_api.domain.user.entity.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserVO, Long> {

    public Optional<UserVO> findOneByEmail(String email);

    public boolean existsByEmail(String email);

    public Page<UserVO> findAllByEmailContains(String email, Pageable pageable);
}
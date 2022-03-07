
package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.user.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserVO, Long> {

    public Optional<UserVO> findOneByEmail(String email);

}

package com.anabada.anabada_api.domain.user.repository;

import com.anabada.anabada_api.domain.user.entity.AuthVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthVO, String> {

}
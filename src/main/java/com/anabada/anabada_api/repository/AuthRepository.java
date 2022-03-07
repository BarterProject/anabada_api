
package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.user.AuthVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthVO, String> {

}
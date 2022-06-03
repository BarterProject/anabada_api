package com.anabada.anabada_api.domain.user.repository;

import com.anabada.anabada_api.domain.user.entity.UserImageVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImageVO, Long> {
    public Optional<UserImageVO> findByName(String name);
}
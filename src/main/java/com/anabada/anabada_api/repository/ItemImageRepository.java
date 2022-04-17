package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.item.ItemImageVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ItemImageRepository extends JpaRepository<ItemImageVO, Long> {

    public Optional<ItemImageVO> findByName(String name);

}

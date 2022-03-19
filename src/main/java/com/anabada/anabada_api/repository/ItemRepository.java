package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.item.ItemVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<ItemVO, Long> {

}

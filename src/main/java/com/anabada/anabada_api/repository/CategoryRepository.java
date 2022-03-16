package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<ItemCategoryVO,Long> {
}

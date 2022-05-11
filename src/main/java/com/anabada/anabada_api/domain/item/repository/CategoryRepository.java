package com.anabada.anabada_api.domain.item.repository;

import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<ItemCategoryVO,Long> {

   public List<ItemCategoryVO> findAllByNameContaining(String name);
}

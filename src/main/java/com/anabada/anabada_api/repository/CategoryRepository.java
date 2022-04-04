package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<ItemCategoryVO,Long> {

   public List<ItemCategoryVO> findAllByNameContaining(String name);
}

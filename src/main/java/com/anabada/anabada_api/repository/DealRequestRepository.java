package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.DealRequestVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DealRequestRepository extends JpaRepository<DealRequestVO, Long> {

    public List<DealRequestVO> findByRequestItemAndResponseItem(ItemVO requestItem, ItemVO responseItem);

}

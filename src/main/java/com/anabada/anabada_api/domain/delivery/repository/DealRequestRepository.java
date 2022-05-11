package com.anabada.anabada_api.domain.delivery.repository;

import com.anabada.anabada_api.domain.item.entity.DealRequestVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DealRequestRepository extends JpaRepository<DealRequestVO, Long> {

    public List<DealRequestVO> findByRequestItemAndResponseItemAndState(ItemVO requestItem, ItemVO responseItem, int state);

    public List<DealRequestVO> findByRequestItem(ItemVO requestItem);

    public List<DealRequestVO> findByResponseItem(ItemVO responseItem);

}

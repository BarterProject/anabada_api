package com.anabada.anabada_api.domain.delivery.repository;

import com.anabada.anabada_api.domain.item.entity.DealRequestVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DealRequestRepository extends JpaRepository<DealRequestVO, Long> {

    public List<DealRequestVO> findByRequestItemAndResponseItemAndState(ItemVO requestItem, ItemVO responseItem, int state);

    public List<DealRequestVO> findByRequestItemAndState(ItemVO requestItem, int state);

    public List<DealRequestVO> findByResponseItemAndState(ItemVO responseItem, int state);

    @Query(value = "select e from DealRequestVO e where ( e.responseItem= :responseItem or e.requestItem =:requestItem ) and e.state=:state order by  e.tradedAt desc ")
    public List<DealRequestVO> findByHistory(@Param("responseItem") ItemVO responseItem, @Param("requestItem") ItemVO requestItem, @Param("state") int state);

}

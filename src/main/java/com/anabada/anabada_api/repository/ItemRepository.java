package com.anabada.anabada_api.repository;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<ItemVO, Long> {

    public List<ItemVO> findByRegistrant(UserVO registrant);

    public List<ItemVO> findByOwner(UserVO Owner);

    public List<ItemVO> findByIdxIn(List<Long> idxList);

}

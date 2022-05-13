package com.anabada.anabada_api.domain.item.service;

import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.item.repository.ItemRepository;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemFindService {
    @Autowired
    ItemRepository itemRepository;
    UserFindService userFindService;

    public ItemFindService(ItemRepository itemRepository, UserFindService userFindService) {
        this.itemRepository = itemRepository;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public ItemVO findByIdx(Long idx) {
        Optional<ItemVO> item = itemRepository.findById(idx);

        if (item.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return item.get();
    }

    @Transactional(readOnly = true)
    public ItemVO findWithAllByIdx(Long idx) {
        Optional<ItemVO> item = itemRepository.findWithAllByIdx(idx);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if (item.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        if(item.get().getOwner() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_NOT_OWN_EXCEPTION);

        return item.get();
    }

    @Transactional(readOnly = true)
    public ItemVO findWithAllByIdxAdmin(Long idx) {
        Optional<ItemVO> item = itemRepository.findWithAllByIdx(idx);

        if (item.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return item.get();
    }


    @Transactional(readOnly = true)
    public List<ItemVO> findByRegistrant(int state) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        if(state == -1)
            return itemRepository.findByRegistrant(user);
        return itemRepository.findByRegistrantAndState(user, state);
    }

    @Transactional(readOnly = true)
    public List<ItemVO> findByOwner(int state) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        if(state == -1)
            return itemRepository.findByOwner(user);
        return itemRepository.findByOwnerAndState(user, state);
    }

    @Transactional(readOnly = true)
    public List<ItemVO> findByRandom(int size) {

        List<Long> idxList = itemRepository.findIdxByState(ItemVO.STATE.APPLIED.ordinal());
        UserVO user = userFindService.getMyUserWithAuthorities();
        List<Long> randomList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Long randNum = idxList.get((int) (Math.random() * idxList.size()));
            randomList.add(randNum);
        }
        List<ItemVO> items = itemRepository.findByIdxList(randomList);

        //내 아이템 제거
        items.removeIf(item -> item.getOwner() == user);

        //random idx가 중복되어 items 수가 size 개수보다 작을 수 있음
        //부족한 수만큼 중복되어 전달될 수 있도록 앞부분에서 복사하여 뒷부분에 추가

        for (int i = 0; size != items.size(); i++)
            items.add(items.get(i % items.size())); // 부족한 item 수가 전체 items 수보다 많을 수 있기 때문에 items.size()로 %연산

        return items;
    }

    @Transactional(readOnly = true)
    public Page<ItemVO> findWithPage(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }


}

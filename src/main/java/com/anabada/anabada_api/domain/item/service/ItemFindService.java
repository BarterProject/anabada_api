package com.anabada.anabada_api.domain.item.service;

import com.anabada.anabada_api.domain.item.dto.ItemDTO;
import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.item.repository.ItemRepository;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemFindService {


    ItemRepository itemRepository;
    UserFindService userFindService;
    CategoryFindService categoryFindService;

    public ItemFindService(ItemRepository itemRepository, UserFindService userFindService, CategoryFindService categoryFindService) {
        this.itemRepository = itemRepository;
        this.userFindService = userFindService;
        this.categoryFindService = categoryFindService;
    }

    @Transactional(readOnly = true)
    public ItemVO findByIdx(Long idx) {
        Optional<ItemVO> item = itemRepository.findById(idx);

        if (item.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return item.get();
    }

    @Transactional(readOnly = true)
    public ItemDTO findWithAllByIdx(Long idx) {
        Optional<ItemVO> item = itemRepository.findWithAllByIdx(idx);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if (item.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        if (item.get().getOwner() != user && item.get().getRegistrant() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_NOT_OWN_EXCEPTION);

        ItemDTO dto = ItemDTO.fromEntityByOwner(item.get());
        return dto;
    }

    @Transactional(readOnly = true)
    public ItemDTO findWithAllByIdxAdmin(Long idx) {
        Optional<ItemVO> item = itemRepository.findWithAllByIdx(idx);

        if (item.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        ItemDTO dto = ItemDTO.allFromEntity(item.get());
        return dto;
    }


    @Transactional(readOnly = true)
    public List<ItemVO> findByRegistrant(int state) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        if (state == -1)
            return itemRepository.findByRegistrant(user);
        return itemRepository.findByRegistrantAndState(user, state);
    }

    @Transactional(readOnly = true)
    public List<ItemVO> findByOwner(int state) {
        UserVO user = userFindService.getMyUserWithAuthorities();

        if (state == -1)
            return itemRepository.findByOwner(user);

        return itemRepository.findByOwnerAndState(user, state);
    }

    @Transactional(readOnly = true)
    public List<ItemVO> findByRandom(int size) {

        UserVO user = userFindService.getMyUserWithAuthorities();
        Page<ItemVO> page = itemRepository.findRandomByStateAndLimit(ItemVO.STATE.APPLIED.ordinal(), user, Pageable.ofSize(size));
        return page.getContent();
    }

    @Transactional(readOnly = true)
    public Page<ItemVO> findWithPage(Pageable pageable, int state) {
        if (state == -1)
            return itemRepository.findAll(pageable);
        return itemRepository.findAllByState(pageable, state);

    }


    @Transactional(readOnly = true)
    public Page<ItemVO> findByItemName(Pageable pageable, String query, int state) {
        if (state == -1)
            return itemRepository.findByNameContains(query, pageable);
        return itemRepository.findByNameContainsAndState(query, pageable, state);
    }

    @Transactional(readOnly = true)
    public Page<ItemVO> findByCategory(Pageable pageable, Long categoryIdx, int state) {
        ItemCategoryVO category = categoryFindService.findByIdx(categoryIdx);

        if (state == -1)
            return itemRepository.findByItemCategory(category, pageable);
        return itemRepository.findByItemCategoryAndState(category, pageable, state);
    }

    @Transactional(readOnly = true)
    public Page<ItemVO> findByOwner(Long userIdx, Pageable pageable) {
        UserVO user = userFindService.findByIdx(userIdx);
        return itemRepository.findByOwner(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ItemVO> findByRegistrant(Long userIdx, Pageable pageable) {
        UserVO user = userFindService.findByIdx(userIdx);
        return itemRepository.findByRegistrant(user, pageable);
    }
}

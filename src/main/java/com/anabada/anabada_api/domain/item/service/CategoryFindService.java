package com.anabada.anabada_api.domain.item.service;


import com.anabada.anabada_api.domain.item.repository.CategoryRepository;
import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryFindService {


    @Autowired
    CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public List<ItemCategoryVO> getAll(){
        return categoryRepository.findAll();
    }


    @Transactional(readOnly = true)
    public ItemCategoryVO findByIdx(Long idx) {

        Optional<ItemCategoryVO> category = categoryRepository.findById(idx);

        if (category.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return category.get();
    }

//    @Transactional(readOnly = true)
//    public ItemCategoryVO existByName(String name) {
//
//        Optional<ItemCategoryVO> category = categoryRepository.findByName(name);
//
//        return category.get();
//    }

    @Transactional(readOnly = true)
    public List<ItemCategoryVO> searchCategory(String name) {
        List<ItemCategoryVO> categoryVO = categoryRepository.findAllByNameContaining(name);

        if (categoryVO.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return categoryVO;
    }
}








package com.anabada.anabada_api.service.item;


import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import com.anabada.anabada_api.repository.CategoryRepository;
import javassist.NotFoundException;
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
    public ItemCategoryVO getByIdx(Long idx) throws NotFoundException {

        Optional<ItemCategoryVO> category = categoryRepository.findById(idx);

        if(category.isEmpty())
            throw new NotFoundException("invalid category idx");

        return category.get();
    }

}

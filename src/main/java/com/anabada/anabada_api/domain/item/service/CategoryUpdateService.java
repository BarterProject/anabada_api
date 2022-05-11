package com.anabada.anabada_api.domain.item.service;


import com.anabada.anabada_api.domain.item.dto.CreateCategory;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.item.dto.ItemCategoryDTO;
import com.anabada.anabada_api.domain.item.repository.CategoryRepository;
import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryUpdateService {


    CategoryRepository categoryRepository;

    CategoryFindService categoryFindService;
    ItemUpdateService itemUpdateService;

    public CategoryUpdateService(CategoryRepository categoryRepository, CategoryFindService categoryFindService, ItemUpdateService itemUpdateService) {
        this.categoryRepository = categoryRepository;
        this.categoryFindService = categoryFindService;
        this.itemUpdateService = itemUpdateService;
    }

    @Transactional
    public ItemCategoryVO save(CreateCategory.Request request) {

        ItemCategoryVO vo = ItemCategoryVO.builder()
                .name(request.getName())
                .build();

        return categoryRepository.save(vo);
    }

    @Transactional
    public void delete(Long categoryIdx) {
        ItemCategoryVO category = categoryFindService.getByIdx(categoryIdx);
        ItemCategoryVO defaultCategory = categoryFindService.getByIdx(7L);

        List<ItemVO> itemList = category.getItemList();
        for(ItemVO item : itemList) {
            item.deleteCategory(defaultCategory);
//            itemUpdateService.save(item);
        }

        categoryRepository.delete(category);
    }




}

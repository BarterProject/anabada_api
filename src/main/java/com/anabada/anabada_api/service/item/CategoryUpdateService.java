package com.anabada.anabada_api.service.item;


import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.item.ItemCategoryDTO;
import com.anabada.anabada_api.repository.CategoryRepository;
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
    public ItemCategoryVO save(ItemCategoryVO vo){
        return categoryRepository.save(vo);
    }

    @Transactional
    public ItemCategoryDTO save(ItemCategoryDTO dto) throws NotFoundException {

        ItemCategoryVO vo = ItemCategoryVO.builder()
                .name(dto.getName())
                .build();

        if(dto.getUpperCategory() != null){
            ItemCategoryVO upper = categoryFindService.getByIdx(dto.getUpperCategory().getIdx());
            vo.setUpperCategory(upper);
        }

        return this.save(vo).dto();
    }

    @Transactional
    public void delete(Long categoryIdx) throws NotFoundException {
        ItemCategoryVO category = categoryFindService.getByIdx(categoryIdx);

        List<ItemVO> itemList = category.getItemList();
        for(ItemVO item : itemList) {
            item.deleteCategory();
            itemUpdateService.save(item);
        }

        categoryRepository.delete(category);
    }




}

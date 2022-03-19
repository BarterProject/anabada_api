package com.anabada.anabada_api.service.item;


import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import com.anabada.anabada_api.dto.item.ItemCategoryDTO;
import com.anabada.anabada_api.repository.CategoryRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CategoryUpdateService {


    CategoryRepository categoryRepository;

    CategoryFindService categoryFindService;

    public CategoryUpdateService(CategoryRepository categoryRepository, CategoryFindService categoryFindService) {
        this.categoryRepository = categoryRepository;
        this.categoryFindService = categoryFindService;
    }

    public ItemCategoryVO save(ItemCategoryVO vo){
        return categoryRepository.save(vo);
    }

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




}

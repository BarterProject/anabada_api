package com.anabada.anabada_api.controller.item;


import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.item.ItemCategoryDTO;
import com.anabada.anabada_api.service.item.CategoryFindService;
import com.anabada.anabada_api.service.item.CategoryUpdateService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CategoryController {


    CategoryUpdateService categoryUpdateService;
    CategoryFindService categoryFindService;


    public CategoryController(CategoryUpdateService categoryUpdateService, CategoryFindService categoryFindService) {
        this.categoryUpdateService = categoryUpdateService;
        this.categoryFindService = categoryFindService;
    }

    /**
     * 카테고리 리스트 반환
     * @param name name : 카테고리 이름 리스트 반환
     * @return ItemCategoryDTO List
     * @throws NotFoundException : 존재하지 않는 카테고리 이름
     */
    @GetMapping("/items/categories")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemCategoryDTO>> getAll(
            @RequestParam(value = "name", defaultValue = "") String name
    ) throws NotFoundException {
        List<ItemCategoryDTO> itemCategory = categoryFindService.searchCategory(name);
       return new ResponseEntity<>(itemCategory, HttpStatus.OK);

    }
    /**
     * 카테고리 등록기능 (관리자만 허용)
     *
     * @param dto name: 카테고리명
     *            upperCategory: 상위 카테고리
     * @return ItemCategoryDTO: 생성된 카테고리
     * @throws NotFoundException 존재하지 않는 upper category idx
     */
    @PostMapping("/items/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ItemCategoryDTO> saveCategory(
            @RequestBody @Validated(ValidationGroups.categorySaveGroup.class) ItemCategoryDTO dto
    ) throws NotFoundException {

        ItemCategoryDTO savedDTO = categoryUpdateService.save(dto);

        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/items/categories/{category-idx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deleteCategory(
            @PathVariable(value = "category-idx") Long categoryIdx
    ) throws NotFoundException {

        categoryUpdateService.delete(categoryIdx);
        return new ResponseEntity<>(new MessageDTO("category deleted"), HttpStatus.NO_CONTENT);
    }


}

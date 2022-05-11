package com.anabada.anabada_api.domain.item.controller;


import com.anabada.anabada_api.domain.item.dto.CreateCategory;
import com.anabada.anabada_api.domain.item.dto.ItemCategoryDTO;
import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import com.anabada.anabada_api.domain.item.service.CategoryFindService;
import com.anabada.anabada_api.domain.item.service.CategoryUpdateService;
import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/items/categories")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemCategoryDTO>> getAll(
            @RequestParam(value = "name", defaultValue = "") String name
    ) {
        List<ItemCategoryVO> itemCategory = categoryFindService.searchCategory(name);
        List<ItemCategoryDTO> dtos = itemCategory.stream().map(ItemCategoryDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);

    }

    @PostMapping("/items/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CreateCategory.Response> saveCategory(
            CreateCategory.Request request
    ) {
        ItemCategoryVO category = categoryUpdateService.save(request);

        return new ResponseEntity<>(new CreateCategory.Response(category.getName()), HttpStatus.CREATED);
    }

    @DeleteMapping("/items/categories/{category-idx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deleteCategory(
            @PathVariable(value = "category-idx") Long categoryIdx
    ) {
        categoryUpdateService.delete(categoryIdx);

        return new ResponseEntity<>(new MessageDTO("category deleted"), HttpStatus.NO_CONTENT);
    }


}

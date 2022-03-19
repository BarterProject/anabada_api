package com.anabada.anabada_api.controller.item;


import com.anabada.anabada_api.domain.item.ItemCategoryVO;
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


    @GetMapping("/items/categories")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemCategoryDTO>> getAll() {

        List<ItemCategoryVO> categories = categoryFindService.getAll();
        List<ItemCategoryDTO> categoriesDTO = categories.stream().map(ItemCategoryVO::dto).collect(Collectors.toList());

        return new ResponseEntity<>(categoriesDTO, HttpStatus.OK);
    }

    @PostMapping("/items/categories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ItemCategoryDTO> saveCategory(
            @RequestBody @Validated(ValidationGroups.categorySaveGroup.class) ItemCategoryDTO dto
    ) throws NotFoundException {

        ItemCategoryDTO savedDTO = categoryUpdateService.save(dto);

        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }


}

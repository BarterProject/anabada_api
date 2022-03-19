package com.anabada.anabada_api.controller.item;


import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.item.ItemUpdateService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {

    ItemUpdateService itemUpdateService;
    ItemFindService itemFindService;

    public ItemController(ItemUpdateService itemUpdateService, ItemFindService itemFindService) {
        this.itemUpdateService = itemUpdateService;
        this.itemFindService = itemFindService;
    }

    @PostMapping("/user/items")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ItemDTO> saveItem(
            @RequestPart(value = "item", required = true) @Validated(ValidationGroups.itemSaveGroup.class) ItemDTO itemDTO,
            @RequestPart(value = "img", required = true) List<MultipartFile> mfList
    ) throws AuthException, NotFoundException, IOException, NotSupportedException {

        ItemDTO savedItem = itemUpdateService.save(itemDTO, mfList);

        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @GetMapping("/user/items")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemDTO>> getMyItems(
        @RequestParam(value = "option", defaultValue = "registrant") String option
    ) throws AuthException, NotFoundException {

        if(option.equals("registrant"))
            return new ResponseEntity<>(itemFindService.findByRegistrant(), HttpStatus.OK);
        else if(option.equals("owner"))
            return new ResponseEntity<>(itemFindService.findByOwner(), HttpStatus.OK);
        else
            throw new NotFoundException("invalid request"); //TODO 예외유형 수정

    }

}

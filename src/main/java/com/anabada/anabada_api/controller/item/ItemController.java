package com.anabada.anabada_api.controller.item;


import com.anabada.anabada_api.domain.DealRequestVO;
import com.anabada.anabada_api.dto.DealRequestDTO;
import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.service.item.DealRequestService;
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
    DealRequestService dealRequestService;

    public ItemController(ItemUpdateService itemUpdateService, ItemFindService itemFindService, DealRequestService dealRequestService) {
        this.itemUpdateService = itemUpdateService;
        this.itemFindService = itemFindService;
        this.dealRequestService = dealRequestService;
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

        if (option.equals("registrant"))
            return new ResponseEntity<>(itemFindService.findByRegistrant(), HttpStatus.OK);
        else if (option.equals("owner"))
            return new ResponseEntity<>(itemFindService.findByOwner(), HttpStatus.OK);
        else
            throw new NotFoundException("invalid request"); //TODO 예외유형 수정

    }


    @GetMapping("/user/items/{item-idx}/requests")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DealRequestDTO>> getRequestsByItem(
            @PathVariable(value = "item-idx") Long itemIdx
    ) throws AuthException, NotFoundException {
        List<DealRequestDTO> dtos = dealRequestService.getRequestsByItem(itemIdx);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/user/items/{item-idx}/responses")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DealRequestDTO>> getResponsesByItem(
            @PathVariable(value = "item-idx") Long itemIdx
    ) throws AuthException, NotFoundException {
        List<DealRequestDTO> dtos = dealRequestService.getResponsesByItem(itemIdx);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/user/items/requests")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<DealRequestDTO> saveRequest(
            @RequestBody @Validated(ValidationGroups.dealRequestGroup.class) DealRequestDTO dto
    ) throws NotFoundException, AuthException {
        DealRequestDTO dealRequestDTO = dealRequestService.save(dto);
        return new ResponseEntity<>(dealRequestDTO, HttpStatus.OK);
    }


    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemDTO>> getRandomItems(
            @RequestParam(value = "size", defaultValue = "10") int size) throws AuthException {
        List<ItemDTO> items = itemFindService.findByRandom(size);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/user/items/requests/{request-idx}/accept")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> acceptRequest(
            @PathVariable(value = "request-idx") Long requestIdx) throws NotFoundException, AuthException {
        dealRequestService.handleRequest(requestIdx, true);
        return new ResponseEntity<>(new MessageDTO("deal accepted"), HttpStatus.OK);
    }

        @PutMapping("/user/items/requests/{request-idx}/decline")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> declineRequest(
            @PathVariable(value = "request-idx") Long requestIdx) throws NotFoundException, AuthException {
        dealRequestService.handleRequest(requestIdx, false);
        return new ResponseEntity<>(new MessageDTO("deal rejected"), HttpStatus.OK);
    }


}








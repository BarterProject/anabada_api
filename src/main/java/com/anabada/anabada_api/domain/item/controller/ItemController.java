package com.anabada.anabada_api.domain.item.controller;


import com.anabada.anabada_api.config.LocalDateTimeSerializer;
import com.anabada.anabada_api.domain.item.dto.*;
import com.anabada.anabada_api.domain.item.entity.DealRequestVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.item.service.DealRequestService;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.item.service.ItemImageService;
import com.anabada.anabada_api.domain.item.service.ItemUpdateService;
import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ItemController {

    ItemUpdateService itemUpdateService;
    ItemFindService itemFindService;
    DealRequestService dealRequestService;
    ItemImageService itemImageService;

    public ItemController(ItemUpdateService itemUpdateService, ItemFindService itemFindService, DealRequestService dealRequestService, ItemImageService itemImageService) {
        this.itemUpdateService = itemUpdateService;
        this.itemFindService = itemFindService;
        this.dealRequestService = dealRequestService;
        this.itemImageService = itemImageService;
    }

    @GetMapping("/v2/items/{item-idx}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ItemDTO> getMyItemByIdx(
            @PathVariable(value = "item-idx") Long itemIdx) {

        ItemVO item = itemFindService.findWithAllByIdx(itemIdx);
        ItemDTO dto = ItemDTO.fromEntityByOwner(item);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/v2/user/items", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CreateItem.Response> saveItem(
            @RequestPart(value = "item", required = true) String itemString,
            @RequestPart(value = "img", required = true) List<MultipartFile> mfList
    ) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();


        CreateItem.Request request = gson.fromJson(itemString, CreateItem.Request.class);
        if (!request.validate()) throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION_VALID_ERROR);

        Long dto = itemUpdateService.save(request, mfList);

        return new ResponseEntity<>(new CreateItem.Response(dto), HttpStatus.CREATED);
    }

    @GetMapping("/v2/user/items")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemDTO>> getMyItems(
            @RequestParam(value = "option", defaultValue = "registrant") String option,
            @RequestParam(value = "state", defaultValue = "-1") int state
    ) {
        List<ItemDTO> dto;

        if (option.equals("registrant")) {
            List<ItemVO> items = itemFindService.findByRegistrant(state);
            dto = items.stream().map(ItemDTO::listFromEntity).collect(Collectors.toList());
        } else if (option.equals("owner")) {
            List<ItemVO> items = itemFindService.findByOwner(state);
            dto = items.stream().map(ItemDTO::listFromEntity).collect(Collectors.toList());
        } else
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @GetMapping("/v2/items")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemDTO>> getRandomItems(
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<ItemVO> items = itemFindService.findByRandom(size);
        List<ItemDTO> dtos = items.stream().map(ItemDTO::listFromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/v2/user/items/requests")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<DealRequest.Response> saveRequest(
            @RequestBody DealRequest.Request request
    ) {
        Long idx = dealRequestService.save(request);
        return new ResponseEntity<>(new DealRequest.Response(idx), HttpStatus.OK);
    }

    @GetMapping("/v2/user/items/{item-idx}/requests")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DealRequestDTO>> getRequestsByItem(
            @PathVariable(value = "item-idx") Long itemIdx,
            @RequestParam(value = "state", defaultValue = "1") int state
    ) {
        List<DealRequestVO> list = dealRequestService.getRequestsByItem(itemIdx, state);
        List<DealRequestDTO> dtos = list.stream().map(DealRequestDTO::fromEntity).collect(Collectors.toList());

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/v2/user/items/{item-idx}/responses")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DealRequestDTO>> getResponsesByItem(
            @PathVariable(value = "item-idx") Long itemIdx,
            @RequestParam(value = "state", defaultValue = "1") int state
    ) {
        List<DealRequestVO> list = dealRequestService.getResponsesByItem(itemIdx, state);
        List<DealRequestDTO> dtos = list.stream().map(DealRequestDTO::fromEntity).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping("/v2/user/items/requests/{request-idx}/accept")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> acceptRequest(
            @PathVariable(value = "request-idx") Long requestIdx) {
        dealRequestService.handleRequest(requestIdx, true);

        return new ResponseEntity<>(new MessageDTO("deal accepted"), HttpStatus.OK);
    }


    @PutMapping("/v2/user/items/requests/{request-idx}/decline")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> declineRequest(
            @PathVariable(value = "request-idx") Long requestIdx) {

        dealRequestService.handleRequest(requestIdx, false);
        return new ResponseEntity<>(new MessageDTO("deal rejected"), HttpStatus.OK);
    }

    @DeleteMapping("/v2/user/items/requests/{request-idx}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deleteRequest(
            @PathVariable(value = "request-idx") Long itemIdx
    ) {
        dealRequestService.delete(itemIdx);
        return new ResponseEntity<>(new MessageDTO("delete success"), HttpStatus.NO_CONTENT);
    }


    @GetMapping(value = "/v2/items/images/{image-name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getItemImageByName(
            @PathVariable(value = "image-name") String itemName
    ) {
        byte[] image = itemImageService.getByName(itemName);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @PostMapping("/v2/user/items/{item-idx}/refund")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> refundRequest(
            @PathVariable(value = "item-idx") Long itemIdx) {
        itemUpdateService.refundRequest(itemIdx);
        return new ResponseEntity<>(new MessageDTO("refund requested"), HttpStatus.OK);
    }

    /* --- 관리자기능 --- */

    @GetMapping(value = "/v2/admin/items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<PageItemDTO> getItemList(
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "mode", defaultValue = "all") String mode,
            @RequestParam(name = "categoryIdx", defaultValue = "0") Long categoryIdx
    ) {
        Page<ItemVO> page;
        switch (mode) {
            case "all":
                page = itemFindService.findWithPage(pageable);
                break;
            case "itemName":
                page = itemFindService.findByItemName(pageable, query);
                break;
            case "category":
                page = itemFindService.findByCategory(pageable, categoryIdx);
                break;
            default:
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }


        List<ItemDTO> dtos = page.getContent().stream().map(ItemDTO::listFromEntity).collect(Collectors.toList());

        PageItemDTO pageDTO = PageItemDTO.builder()
                .items(dtos)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/v2/admin/users/{user-idx}/items")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<PageItemDTO> getItemList(
            @PathVariable(value = "user-idx") Long userIdx,
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "mode", defaultValue = "registrant") String mode
    ) {
        Page<ItemVO> page;

        if(mode.equals("registrant")){
            page = itemFindService.findByRegistrant(userIdx, pageable);
        }else if(mode.equals("owner")){
            page = itemFindService.findByOwner(userIdx, pageable);
        }else
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        List<ItemDTO> dtos = page.getContent().stream().map(ItemDTO::listFromEntity).collect(Collectors.toList());

        PageItemDTO pageDTO = PageItemDTO.builder()
                .items(dtos)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/v2/admin/items/{item-idx}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ItemDTO> getItemByIdx(
            @PathVariable(value = "item-idx") Long itemIdx
    ) {
        ItemVO item = itemFindService.findWithAllByIdxAdmin(itemIdx);
        ItemDTO dto = ItemDTO.allFromEntity(item);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/v2/admin/items/{item-idx}/deactivation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deactivateItem(
            @PathVariable(value = "item-idx") Long itemIdx
    ) {
        itemUpdateService.activateItem(itemIdx, false);
        return new ResponseEntity<>(new MessageDTO("item deactivated"), HttpStatus.OK);
    }

    @PutMapping("/v2/admin/items/{item-idx}/activation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> activateItem(
            @PathVariable(value = "item-idx") Long itemIdx
    ) {
        itemUpdateService.activateItem(itemIdx, true);
        return new ResponseEntity<>(new MessageDTO("item activated"), HttpStatus.OK);
    }

    @PutMapping("/v2/admin/items/{item-idx}/refund")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> refundComplete(
            @PathVariable(value = "item-idx") Long itemIdx) {
        itemUpdateService.refundComplete(itemIdx);
        return new ResponseEntity<>(new MessageDTO("refund complete"), HttpStatus.OK);
    }


}








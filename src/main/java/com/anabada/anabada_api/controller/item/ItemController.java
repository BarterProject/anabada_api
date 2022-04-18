package com.anabada.anabada_api.controller.item;


import com.anabada.anabada_api.config.LocalDateTimeSerializer;
import com.anabada.anabada_api.dto.DealRequestDTO;
import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.service.item.DealRequestService;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.item.ItemImageService;
import com.anabada.anabada_api.service.item.ItemUpdateService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 아이템 등록
     *
     * @param itemDTO 아이템 정보
     *                name: 아이템명
     *                description: 아이템 설명
     *                clause_agree: 약관 동의여부
     *                payment: 지불정보
     *                itemCategory: 카테고리 번호
     * @param mfList  img(multipartfile) list
     * @return ItemDTO: 등록된 물건
     * @throws AuthException         유효하지 않은 토큰
     * @throws NotFoundException     유효하지 않은 category idx
     *                               유효하지 않은 payment option idx
     * @throws IOException           파일저장 오류
     * @throws NotSupportedException 지원하지 않는 이미지 파일유형(jpg,jpeg,png,bmp만 지원)
     */
    @PostMapping(value = "/user/items", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ItemDTO> saveItem(
//            @RequestPart(value = "item", required = true) @Validated(ValidationGroups.itemSaveGroup.class) ItemDTO itemDTO,
            @RequestPart(value = "item", required = true) String itemString,
            @RequestPart(value = "img", required = true) List<MultipartFile> mfList
    ) throws AuthException, NotFoundException, IOException, NotSupportedException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        ItemDTO itemDTO = gson.fromJson(itemString, ItemDTO.class);

        ItemDTO savedItem = itemUpdateService.save(itemDTO, mfList);

        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    /**
     * 아이템 리스트 반환
     *
     * @param option option -> registrant: 내가 등록한 물건 리스트 반환
     *               option -> owner: 내가 소유한 물건 리스트 반환
     * @return ItemDTO List
     * @throws AuthException     유효하지 않은 토큰
     * @throws NotFoundException option 파라미터 오류 (에러유형 수정해야함)
     */
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

    /**
     * 특정 아이템이 받은 거래 요청 리스트 반환
     *
     * @param itemIdx item의 idx
     * @return 거래요청 리스트
     * @throws AuthException     유효하지 않은 토큰
     *                           소유하지 않은 아이템
     * @throws NotFoundException 유효하지 않은 item idx
     */
    @GetMapping("/user/items/{item-idx}/requests")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DealRequestDTO>> getRequestsByItem(
            @PathVariable(value = "item-idx") Long itemIdx
    ) throws AuthException, NotFoundException {
        List<DealRequestDTO> dtos = dealRequestService.getRequestsByItem(itemIdx);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * 특정 아이템이 보낸 거래 요청 리스트 반환
     *
     * @param itemIdx item의 idx
     * @return 거래요청 리스트
     * @throws AuthException     유효하지 않은 토큰
     *                           소유하지 않은 아이템
     * @throws NotFoundException 유효하지 않은 item idx
     */
    @GetMapping("/user/items/{item-idx}/responses")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DealRequestDTO>> getResponsesByItem(
            @PathVariable(value = "item-idx") Long itemIdx
    ) throws AuthException, NotFoundException {
        List<DealRequestDTO> dtos = dealRequestService.getResponsesByItem(itemIdx);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * 거래요청
     *
     * @param dto requestItem: 요청하는 아이템 8dx
     *            responseItem: 요청받는 아이템 idx
     * @return 거래요청 정보
     * @throws NotFoundException 유효하지 않은 request item idx
     *                           유효하지 않은 response item idx
     * @throws AuthException     유효하지 않은 토큰
     */
    @PostMapping("/user/items/requests")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<DealRequestDTO> saveRequest(
            @RequestBody @Validated(ValidationGroups.dealRequestGroup.class) DealRequestDTO dto
    ) throws NotFoundException, AuthException, URISyntaxException {
        DealRequestDTO dealRequestDTO = dealRequestService.save(dto);
        return new ResponseEntity<>(dealRequestDTO, HttpStatus.OK);
    }

    /**
     * 랜덤 아이템 리스트 반환
     *
     * @param size 반환 아이템 개수 (default 10)
     * @return ItemDTO 리스트
     */
    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<ItemDTO>> getRandomItems(
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<ItemDTO> items = itemFindService.findByRandom(size);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    /**
     * 거래요청 수락
     *
     * @param requestIdx 거래요청의 idx
     * @return 성공 message
     * @throws NotFoundException 유효하지 않은 request idx
     * @throws AuthException     유효하지 않은 토큰
     *                           요청 클라이언트 != 아이템 소유자
     */
    @PutMapping("/user/items/requests/{request-idx}/accept")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> acceptRequest(
            @PathVariable(value = "request-idx") Long requestIdx) throws NotFoundException, AuthException {
        dealRequestService.handleRequest(requestIdx, true);
        return new ResponseEntity<>(new MessageDTO("deal accepted"), HttpStatus.OK);
    }

    /**
     * 거래요청 거절
     *
     * @param requestIdx
     * 거래요청의 idx
     * @return 실패 message
     * @throws NotFoundException 유효하지 않은 request idx
     * @throws AuthException     유효하지 않은 토큰
     *                           요청 클라이언트 != 아이템 소유자
     */
    @PutMapping("/user/items/requests/{request-idx}/decline")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> declineRequest(
            @PathVariable(value = "request-idx") Long requestIdx) throws NotFoundException, AuthException {
        dealRequestService.handleRequest(requestIdx, false);
        return new ResponseEntity<>(new MessageDTO("deal rejected"), HttpStatus.OK);
    }


    @GetMapping(value = "/items/images/{image-name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getItemImageByName(
            @PathVariable(value = "image-name") String itemName
    ) throws NotFoundException, IOException {
        byte[] image = itemImageService.getByName(itemName);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

}








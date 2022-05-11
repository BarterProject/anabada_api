package com.anabada.anabada_api.domain.item.service;

import com.anabada.anabada_api.domain.item.dto.DealRequest;
import com.anabada.anabada_api.domain.item.entity.DealRequestVO;
import com.anabada.anabada_api.domain.message.entity.NoticeVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.delivery.repository.DealRequestRepository;
import com.anabada.anabada_api.domain.message.service.NoticeUpdateService;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DealRequestService {

    DealRequestRepository dealRequestRepository;

    UserFindService userFindService;
    ItemFindService itemFindService;
    ItemUpdateService itemUpdateService;
    NoticeUpdateService noticeUpdateService;

    public DealRequestService(DealRequestRepository dealRequestRepository, UserFindService userFindService, ItemFindService itemFindService, ItemUpdateService itemUpdateService, NoticeUpdateService noticeUpdateService) {
        this.dealRequestRepository = dealRequestRepository;
        this.userFindService = userFindService;
        this.itemFindService = itemFindService;
        this.itemUpdateService = itemUpdateService;
        this.noticeUpdateService = noticeUpdateService;
    }

    @Transactional
    public DealRequestVO save(DealRequestVO vo) {

        UserVO responseUser = vo.getResponseItem().getOwner();
        NoticeVO noticeVO = NoticeVO.builder()
                .route("/item/~~")
                .kind("item request")
                .content("아이템 거래 요청왔어용")
                .state(1L)
                .user(responseUser)
                .build();

//        noticeUpdateService.save(noticeVO);
        return dealRequestRepository.save(vo);
    }

    @Transactional
    public DealRequestVO save(DealRequest.Request request){

        UserVO user = userFindService.getMyUserWithAuthorities();

        ItemVO requestItem = itemFindService.findByIdx(request.getRequestItemIdx());
        ItemVO response = itemFindService.findByIdx(request.getResponseItemIdx());


        if (requestItem.getOwner() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        if (response.getOwner() == user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        if (response.getState() != 1)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        if(this.dealRequestDuplicateCheck(requestItem, response))
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);


        DealRequestVO dealVO = DealRequestVO.builder()
                .requestItem(requestItem)
                .responseItem(response)
                .state(DealRequestVO.STATE.ONGOING.ordinal())
                .build();

        return this.save(dealVO);
    }

    @Transactional
    public void handleRequest(Long requestIdx, Boolean accept) {

        DealRequestVO request = this.getByIdx(requestIdx);
        UserVO responseUser = userFindService.getMyUserWithAuthorities();
        UserVO requestUser = request.getRequestItem().getOwner();

        if(request.getResponseItem().getOwner() != responseUser)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        if(accept){
            request.getRequestItem().changeOwner(responseUser);
            request.getResponseItem().changeOwner(requestUser);
        }

        this.closeRequestByItem(request.getRequestItem());
        this.closeRequestByItem(request.getResponseItem());

    }

    @Transactional
    public void closeRequestByItem(ItemVO item){
        List<DealRequestVO> requestItems = item.getDealRequestItemList();
        List<DealRequestVO> responseItems = item.getDealResponseItemList();

        for(DealRequestVO request : requestItems)
            request.close();

        for(DealRequestVO request : responseItems)
            request.close();

    }

    @Transactional(readOnly = true)
    public boolean dealRequestDuplicateCheck(ItemVO request, ItemVO response) {
        List<DealRequestVO> requests = dealRequestRepository.findByRequestItemAndResponseItemAndState(request, response, 1);
        return requests.size() > 0;
    }

    @Transactional(readOnly = true)
    public List<DealRequestVO> getRequestsByItem(Long itemIdx){
        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if (user != item.getOwner())
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);


        return  dealRequestRepository.findByRequestItem(item);
//        return item.getDealRequestItemList();
    }

    @Transactional(readOnly = true)
    public List<DealRequestVO> getResponsesByItem(Long itemIdx) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if (user != item.getOwner()) {
            throw new BadCredentialsException("not your own item");
        }

        return  dealRequestRepository.findByResponseItem(item);
    }

    @Transactional(readOnly = true)
    public DealRequestVO getByIdx(Long idx) {
        Optional<DealRequestVO> vo = dealRequestRepository.findById(idx);

        if(vo.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return vo.get();
    }




}

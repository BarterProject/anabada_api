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
import com.anabada.anabada_api.firebase.FCMService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DealRequestService {

    DealRequestRepository dealRequestRepository;

    UserFindService userFindService;
    ItemFindService itemFindService;

    public DealRequestService(DealRequestRepository dealRequestRepository, UserFindService userFindService, ItemFindService itemFindService) {
        this.dealRequestRepository = dealRequestRepository;
        this.userFindService = userFindService;
        this.itemFindService = itemFindService;
    }

    @Transactional
    public DealRequestVO save(DealRequestVO vo) {

        UserVO responseUser = vo.getResponseItem().getOwner();


        return dealRequestRepository.save(vo);
    }

    @Transactional
    public Long save(DealRequest.Request request) {

        UserVO user = userFindService.getMyUserWithAuthorities();

        ItemVO requestItem = itemFindService.findByIdx(request.getRequestItemIdx());
        ItemVO response = itemFindService.findByIdx(request.getResponseItemIdx());


        if (requestItem.getOwner() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_NOT_OWN_EXCEPTION);

        if (response.getOwner() == user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        if (response.getState() != ItemVO.STATE.APPLIED.ordinal() || requestItem.getState() != ItemVO.STATE.APPLIED.ordinal())
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        if (this.dealRequestDuplicateCheck(requestItem, response))
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION_REQUEST_DUPLICATED);


        DealRequestVO dealVO = DealRequestVO.builder()
                .requestItem(requestItem)
                .responseItem(response)
                .state(DealRequestVO.STATE.ACTIVATED.ordinal())
                .build();

        return this.save(dealVO).getIdx();
    }


    @Transactional
    public void delete(Long requestIdx) {
        DealRequestVO request = this.findByIdx(requestIdx);
        UserVO user = userFindService.getMyUserWithAuthorities();

        if (request.getRequestItem().getOwner() != user)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        dealRequestRepository.delete(request);
    }

    @Transactional
    public void handleRequest(Long requestIdx, Boolean accept) {

        DealRequestVO request = this.getByIdx(requestIdx);
        UserVO responseUser = userFindService.getMyUserWithAuthorities();
        UserVO requestUser = request.getRequestItem().getOwner();

        if (request.getResponseItem().getOwner() != responseUser)
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);

        if (request.getState() != DealRequestVO.STATE.ACTIVATED.ordinal())
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        if (request.getCreatedAt().plusMinutes(1L).isBefore(LocalDateTime.now()))
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        this.closeRequestByItem(request.getRequestItem());
        this.closeRequestByItem(request.getResponseItem());

        if (accept) {
            request.getRequestItem().changeOwner(responseUser);
            request.getResponseItem().changeOwner(requestUser);
            request.setState(DealRequestVO.STATE.ACCOMPLISHED.ordinal());
        } else request.setState(DealRequestVO.STATE.DENIED.ordinal());


    }

    @Transactional
    public void closeRequestByItem(ItemVO item) {
        List<DealRequestVO> requestItems = item.getDealRequestItemList();
        List<DealRequestVO> responseItems = item.getDealResponseItemList();

        for (DealRequestVO request : requestItems)
            request.close();

        for (DealRequestVO request : responseItems)
            request.close();

    }


    @Transactional(readOnly = true)
    public DealRequestVO findByIdx(Long idx) {
        return dealRequestRepository.findById(idx).orElseThrow(() -> new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION));
    }

    @Transactional(readOnly = true)
    public boolean dealRequestDuplicateCheck(ItemVO request, ItemVO response) {
        List<DealRequestVO> requests = dealRequestRepository.findByRequestItemAndResponseItemAndState(request, response, 1);
        return requests.size() > 0;
    }

    @Transactional(readOnly = true)
    public List<DealRequestVO> getRequestsByItem(Long itemIdx, int state) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if (user != item.getOwner())
            throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);


        return dealRequestRepository.findByRequestItemAndState(item, state);
    }

    @Transactional(readOnly = true)
    public List<DealRequestVO> getResponsesByItem(Long itemIdx, int state) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if (user != item.getOwner()) {
            throw new BadCredentialsException("not your own item");
        }

        return dealRequestRepository.findByResponseItemAndState(item, state);
    }

    @Transactional(readOnly = true)
    public DealRequestVO getByIdx(Long idx) {
        Optional<DealRequestVO> vo = dealRequestRepository.findById(idx);

        if (vo.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return vo.get();
    }




}

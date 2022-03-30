package com.anabada.anabada_api.service.item;

import com.anabada.anabada_api.domain.DealRequestVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.DealRequestDTO;
import com.anabada.anabada_api.repository.DealRequestRepository;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealRequestService {

    DealRequestRepository dealRequestRepository;

    UserFindService userFindService;
    ItemFindService itemFindService;
    ItemUpdateService itemUpdateService;

    public DealRequestService(DealRequestRepository dealRequestRepository, UserFindService userFindService, ItemFindService itemFindService, ItemUpdateService itemUpdateService) {
        this.dealRequestRepository = dealRequestRepository;
        this.userFindService = userFindService;
        this.itemFindService = itemFindService;
        this.itemUpdateService = itemUpdateService;
    }

    @Transactional
    public DealRequestVO save(DealRequestVO vo) {
        return dealRequestRepository.save(vo);
    }

    @Transactional
    public DealRequestDTO save(DealRequestDTO dto) throws NotFoundException, AuthException {

        UserVO user = userFindService.getMyUserWithAuthorities();

        ItemVO request = itemFindService.findByIdx(dto.getRequestItem().getIdx());
        ItemVO response = itemFindService.findByIdx(dto.getResponseItem().getIdx());


        if (request.getOwner() != user)
            throw new BadCredentialsException("not your own item (request)");

        if (response.getOwner() == user)
            throw new BadCredentialsException("you can't request deal to your own item");

        if (response.getState() != 1)
            throw new BadCredentialsException("response item is not shared");

        if(this.dealRequestDuplicateCheck(request, response))
            throw new BadCredentialsException("same request already exists.");


        DealRequestVO dealVO = DealRequestVO.builder()
                .requestItem(request)
                .responseItem(response)
                .state(1L)
                .build();

        return this.save(dealVO).dto(true, true);
    }

    @Transactional
    public void handleRequest(Long requestIdx, Boolean accept) throws NotFoundException, AuthException {

        DealRequestVO request = this.getByIdx(requestIdx);
        UserVO responseUser = userFindService.getMyUserWithAuthorities();
        UserVO requestUser = request.getRequestItem().getOwner();

        if(request.getResponseItem().getOwner() != responseUser)
            throw new AuthException("only owner can handle this request");

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

        itemUpdateService.save(item);
    }

    @Transactional(readOnly = true)
    public boolean dealRequestDuplicateCheck(ItemVO request, ItemVO response) {
        List<DealRequestVO> requests = dealRequestRepository.findByRequestItemAndResponseItemAndState(request, response, 1L);
        return requests.size() > 0;
    }

    @Transactional(readOnly = true)
    public List<DealRequestDTO> getRequestsByItem(Long itemIdx) throws AuthException, NotFoundException {
        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if (user != item.getOwner()) {
            throw new BadCredentialsException("not your own item");
        }

        return item.getDealRequestItemList().stream().map(i -> i.dto(true, true)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DealRequestDTO> getResponsesByItem(Long itemIdx) throws AuthException, NotFoundException {
        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = itemFindService.findByIdx(itemIdx);

        if (user != item.getOwner()) {
            throw new BadCredentialsException("not your own item");
        }

        return item.getDealResponseItemList().stream().map(i -> i.dto(true, true)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DealRequestVO getByIdx(Long idx) throws NotFoundException {
        Optional<DealRequestVO> vo = dealRequestRepository.findById(idx);

        if(vo.isEmpty())
            throw new NotFoundException("invalid idx of request");

        return vo.get();
    }




}

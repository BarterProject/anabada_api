package com.anabada.anabada_api.service.item;

import com.anabada.anabada_api.domain.DealRequestVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.DealRequestDTO;
import com.anabada.anabada_api.repository.DealRequestRepository;
import com.anabada.anabada_api.service.user.UserFindService;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public boolean dealRequestDuplicateCheck(ItemVO request, ItemVO response) {
        List<DealRequestVO> requests = dealRequestRepository.findByRequestItemAndResponseItem(request, response);
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


}

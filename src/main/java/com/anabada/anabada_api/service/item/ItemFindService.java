package com.anabada.anabada_api.service.item;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.dto.user.UserDTO;
import com.anabada.anabada_api.repository.ItemRepository;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemFindService {
    @Autowired
    ItemRepository itemRepository;
    UserFindService userFindService;

    public ItemFindService(ItemRepository itemRepository, UserFindService userFindService) {
        this.itemRepository = itemRepository;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public ItemVO findByIdx(Long idx) throws NotFoundException {
        Optional<ItemVO> item = itemRepository.findById(idx);

        if (item.isEmpty())
            throw new NotFoundException("Invalid");

        return item.get();
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findByRegistrant() throws AuthException {
        UserVO user = userFindService.getMyUserWithAuthorities();

        return itemRepository.findByRegistrant(user).stream().map(i -> i.dto(true, true, true, true, true)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findByOwner() throws AuthException {
        UserVO user = userFindService.getMyUserWithAuthorities();
        return itemRepository.findByOwner(user).stream().map(i -> i.dto(true, true, true, true, true)).collect(Collectors.toList());
    }


}

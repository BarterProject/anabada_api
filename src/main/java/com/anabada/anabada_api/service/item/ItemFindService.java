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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
            throw new NotFoundException("Invalid idx");

        return item.get();
    }

    @Transactional(readOnly = true)
    public ItemDTO findItemDTOByIdx(Long idx) throws NotFoundException, AuthException {

        //TODO 유저권한에 따른 정보 차별적 전달 좀더 세분화 해야함

        UserVO user = userFindService.getMyUserWithAuthorities();
        ItemVO item = this.findByIdx(idx);

        if(item.getOwner() == user)
            return item.dto(false, true, false, true, true, false);
        else{
            ItemDTO dto = item.dto(false, false, false, true, true, false);
            dto.setOwner(item.getOwner().dto(false));
            return dto;
        }

    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findByRegistrant() throws AuthException {
        UserVO user = userFindService.getMyUserWithAuthorities();

        return itemRepository.findByRegistrant(user).stream().map(i -> i.dto(true, true, true, true, true, false)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findByOwner() throws AuthException {
        UserVO user = userFindService.getMyUserWithAuthorities();
        return itemRepository.findByOwner(user).stream().map(i -> i.dto(true, true, true, true, true, false)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findByRandom(int size) {

        List<Long> idxList = itemRepository.findIdxByState(1L);
        List<Long> randomList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Long randNum = idxList.get((int) (Math.random() * idxList.size()));
            randomList.add(randNum);
        }
        List<ItemVO> items = itemRepository.findByIdxList(randomList);

        //random idx가 중복되어 items 수가 size 개수보다 작을 수 있음
        //부족한 수만큼 중복되어 전달될 수 있도록 앞부분에서 복사하여 뒷부분에 추가

        for (int i = 0; size != items.size(); i++)
            items.add(items.get(i % items.size())); // 부족한 item 수가 전체 items 수보다 많을 수 있기 때문에 items.size()로 %연산


        return items.stream().map(i -> i.dto(true, true, false, true, true, false)).collect(Collectors.toList());
    }


}

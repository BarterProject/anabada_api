package com.anabada.anabada_api.service.item;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.repository.ItemRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ItemFindService {
    @Autowired
    ItemRepository itemRepository;

    public ItemFindService(ItemRepository itemRepository){
        this.itemRepository=itemRepository;
    }

    @Transactional(readOnly = true)
    public ItemVO findByIdx(Long idx)throws NotFoundException{
        Optional<ItemVO>item=itemRepository.findById(idx);

        if(item.isEmpty())
            throw new NotFoundException("Invalid");

        return item.get();
    }

    @Transactional(readOnly = true)
    public ItemDTO findByIdxDTO(Long idx)throws NotFoundException{
       ItemVO item=this.findByIdx(idx);

        return item.dto();
    }

}

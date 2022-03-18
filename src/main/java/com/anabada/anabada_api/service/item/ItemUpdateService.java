package com.anabada.anabada_api.service.item;

import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.repository.ItemRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ItemUpdateService {
    @Autowired
    ItemRepository itemRepository;

    public ItemUpdateService(ItemRepository itemRepository){
        this.itemRepository=itemRepository;
    }

    @Transactional
    public ItemVO save(ItemVO item)throws NotFoundException{
        return itemRepository.save(item);
    }



}

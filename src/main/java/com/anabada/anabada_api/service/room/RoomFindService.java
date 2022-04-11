package com.anabada.anabada_api.service.room;

import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.repository.RoomRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoomFindService {
    RoomRepository roomRepository;

    public RoomFindService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional(readOnly = true)
    public RoomVO findByIdx(Long idx)throws NotFoundException {
        Optional<RoomVO>roomVO= roomRepository.findById(idx);
        if(roomVO.isEmpty())
            throw new NotFoundException("invalid");
        return roomVO.get();
    }

}

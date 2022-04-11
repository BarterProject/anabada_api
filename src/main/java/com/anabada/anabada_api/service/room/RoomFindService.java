package com.anabada.anabada_api.service.room;

import com.anabada.anabada_api.domain.message.RoomUserVO;
import com.anabada.anabada_api.domain.message.RoomVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.room.RoomDTO;
import com.anabada.anabada_api.repository.RoomRepository;
import com.anabada.anabada_api.repository.RoomUserRepository;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomFindService {
    RoomUserRepository roomUserRepository;
    UserFindService userFindService;

    public RoomFindService(RoomUserRepository roomUserRepository, UserFindService userFindService) {
        this.roomUserRepository = roomUserRepository;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public List<RoomDTO> getMyRooms() throws AuthException {
        UserVO user = userFindService.getMyUserWithAuthorities();
        List<RoomUserVO>  mappings = roomUserRepository.findAllByUser(user);

        List<RoomVO> rooms = mappings.stream().map(RoomUserVO::getRoom).collect(Collectors.toList());
        return rooms.stream().map(i -> i.dto(false)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomVO getByIdx(Long idx) throws NotFoundException {
        Optional<RoomUserVO> optional = roomUserRepository.findById(idx);

        if(optional.isEmpty())
            throw new NotFoundException("invalid room idx");

        return optional.get().getRoom();
    }

}

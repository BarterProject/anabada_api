package com.anabada.anabada_api.domain.message.service;

import com.anabada.anabada_api.domain.message.entity.RoomUserVO;
import com.anabada.anabada_api.domain.message.entity.RoomVO;
import com.anabada.anabada_api.domain.message.dto.RoomDTO;
import com.anabada.anabada_api.domain.message.repository.RoomRepository;
import com.anabada.anabada_api.domain.message.repository.RoomUserRepository;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
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
    RoomRepository roomRepository;

    UserFindService userFindService;


    public RoomFindService(RoomUserRepository roomUserRepository, RoomRepository roomRepository, UserFindService userFindService) {
        this.roomUserRepository = roomUserRepository;
        this.roomRepository = roomRepository;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public List<RoomVO> getMyRooms(){
        UserVO user = userFindService.getMyUserWithAuthorities();
        List<RoomUserVO>  mappings = roomUserRepository.findAllByUser(user);

        return mappings.stream().map(RoomUserVO::getRoom).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomVO getByIdx(Long idx) {
        Optional<RoomVO> optional = roomRepository.findById(idx);

        if(optional.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return optional.get();
    }

    @Transactional(readOnly = true)
    public RoomVO getByName(String name){
        Optional<RoomVO> optional = roomRepository.findByName(name);

        if(optional.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return optional.get();
    }

}

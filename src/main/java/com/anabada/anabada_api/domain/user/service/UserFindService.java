package com.anabada.anabada_api.domain.user.service;

import com.anabada.anabada_api.domain.user.repository.UserRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserFindService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserVO getMyUserWithAuthorities() {
        Optional<UserVO> memberVO = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneByEmail);

        if(memberVO.isEmpty())
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        return memberVO.get();
    }

    @Transactional(readOnly = true)
    public UserVO findByIdx(Long idx){
        Optional<UserVO>userVO= userRepository.findById(idx);

        if(userVO.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return userVO.get();
    }

    @Transactional(readOnly = true)
    public UserVO findByIdxDTO(Long idx){
        Optional<UserVO>userVO= userRepository.findById(idx);

        if(userVO.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return userVO.get();
    }

    @Transactional(readOnly = true)
    public Page<UserVO> findAll(Pageable pageable){
        return userRepository.findAll(pageable);

//        return PageUserDTO.builder()
//                .users(users)
//                .currentPage(pageable.getPageNumber())
//                .totalPage(page.getTotalPages() - 1)
//                .build();

    }
}

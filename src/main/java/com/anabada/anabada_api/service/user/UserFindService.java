package com.anabada.anabada_api.service.user;

import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.user.PageUserDTO;
import com.anabada.anabada_api.dto.user.UserDTO;
import com.anabada.anabada_api.repository.UserRepository;
import com.anabada.anabada_api.util.SecurityUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFindService {

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserVO getMyUserWithAuthorities() throws AuthException {
        Optional<UserVO> memberVO = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneByEmail);

        if(memberVO.isEmpty())
            throw new AuthException("invalid member or token");

        return memberVO.get();
    }

    @Transactional(readOnly = true)
    public UserVO findByIdx(Long idx)throws NotFoundException{
        Optional<UserVO>userVO= userRepository.findById(idx);
        if(userVO.isEmpty())
            throw new NotFoundException("invalid");
        return userVO.get();
    }

    @Transactional(readOnly = true)
    public UserDTO findByIdxDTO(Long idx)throws NotFoundException{
        Optional<UserVO>userVO= userRepository.findById(idx);
        if(userVO.isEmpty())
            throw new NotFoundException("invalid");
        return userVO.get().dto(true);
    }

    @Transactional(readOnly = true)
    public PageUserDTO findAll(Pageable pageable){
        Page<UserVO> page = userRepository.findAll(pageable);
        List<UserDTO> users = page.stream().map(i -> i.dto(false)).collect(Collectors.toList());

        return PageUserDTO.builder()
                .users(users)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

    }
}

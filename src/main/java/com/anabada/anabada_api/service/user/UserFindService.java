package com.anabada.anabada_api.service.user;

import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.repository.UserRepository;
import com.anabada.anabada_api.util.SecurityUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.Optional;

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
}

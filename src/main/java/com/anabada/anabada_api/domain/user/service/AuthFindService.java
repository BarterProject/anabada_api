package com.anabada.anabada_api.domain.user.service;

import com.anabada.anabada_api.domain.user.repository.AuthRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.anabada.anabada_api.domain.user.entity.AuthVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class AuthFindService {

    @Autowired
    AuthRepository authRepository;

    @Transactional(readOnly = true)
    public AuthVO getByName(String name) {
        Optional<AuthVO> auth = authRepository.findById(name);

        if(auth.isEmpty())
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        return auth.get();
    }

}

package com.anabada.anabada_api.domain.user.service;


import com.anabada.anabada_api.domain.user.dto.CreateUser;
import com.anabada.anabada_api.domain.user.repository.UserRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.anabada.anabada_api.domain.user.entity.AuthVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserUpdateService {

    UserRepository userRepository;

    UserFindService userFindService;
    private final PasswordEncoder passwordEncoder;
    AuthFindService authFindService;

    public UserUpdateService(UserRepository userRepository, UserFindService userFindService, PasswordEncoder passwordEncoder, AuthFindService authFindService) {
        this.userRepository = userRepository;
        this.userFindService = userFindService;
        this.passwordEncoder = passwordEncoder;
        this.authFindService = authFindService;
    }



    @Transactional
    public Long signUp(CreateUser.Request request){

        if (userRepository.existsByEmail(request.getEmail()))
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION_MEMBER_DUPLICATED);

        AuthVO auth = authFindService.getByName(AuthVO.Type.USER.value());

        UserVO vo = UserVO.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .bankAccount(request.getBankAccount())
                .bankKind(request.getBankKind())
                .activated(UserVO.Activated.ACTIVATED.value())
                .oauth(UserVO.Oauth.DEFAULT.value())
                .auth(auth)
                .build();

        return userRepository.save(vo).getIdx();
    }

    @Transactional
    public void activateUser(Long userIdx, boolean isActivate) {
        UserVO user = userFindService.findByIdx(userIdx);
        user.activate(isActivate);
    }

}

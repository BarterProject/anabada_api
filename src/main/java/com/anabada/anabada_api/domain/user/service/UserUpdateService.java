package com.anabada.anabada_api.domain.user.service;


import com.anabada.anabada_api.domain.user.dto.ApplyFCMToken;
import com.anabada.anabada_api.domain.user.dto.CreateUser;
import com.anabada.anabada_api.domain.user.entity.AuthVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.repository.UserRepository;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserUpdateService {

    private final PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserFindService userFindService;
    AuthFindService authFindService;
    UserImageService userImageService;

    public UserUpdateService(UserRepository userRepository, UserFindService userFindService, PasswordEncoder passwordEncoder, AuthFindService authFindService, UserImageService userImageService) {
        this.userRepository = userRepository;
        this.userFindService = userFindService;
        this.passwordEncoder = passwordEncoder;
        this.authFindService = authFindService;
        this.userImageService = userImageService;
    }


    @Transactional
    public Long signUp(CreateUser.Request request) {

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
    public UserVO saveUserImage(MultipartFile mf) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        userImageService.save(mf, user);
        return user;
    }

    @Transactional
    public void activateUser(Long userIdx, boolean isActivate) {
        UserVO user = userFindService.findByIdx(userIdx);
        user.activate(isActivate);
    }

    @Transactional
    public void updateFCMToken(ApplyFCMToken.Request request) {
        UserVO user = userFindService.getMyUserWithAuthorities();
        user.setFcm(request.getToken());
    }

    @Transactional
    public void deleteFCMToken() {
        UserVO user = userFindService.getMyUserWithAuthorities();
        user.deleteToken();
    }
}

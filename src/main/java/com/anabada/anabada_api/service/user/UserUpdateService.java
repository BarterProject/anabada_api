package com.anabada.anabada_api.service.user;


import com.anabada.anabada_api.domain.user.AuthType;
import com.anabada.anabada_api.domain.user.AuthVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.user.UserDTO;
import com.anabada.anabada_api.repository.UserRepository;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;
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
    public UserVO save(UserVO vo){
        return userRepository.save(vo);
    }

    @Transactional
    public UserDTO signUp(UserDTO dto) throws DuplicateMemberException, NotFoundException {

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new DuplicateMemberException("already exist user email");

        AuthVO auth = authFindService.getByName(AuthType.USER.getName());

        UserVO vo = UserVO.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .bankAccount(dto.getBankAccount())
                .bankKind(dto.getBankKind())
                .activated(true)
                .oauth("default")
                .auth(auth)
                .build();

        return this.save(vo).dto(true);

    }

    @Transactional
    public void activateUser(Long userIdx, boolean isActivate) throws NotFoundException {
        UserVO user = userFindService.findByIdx(userIdx);
        user.activate(isActivate);
        this.save(user);
    }

}

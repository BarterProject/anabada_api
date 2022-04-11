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

@Service
public class UserUpdateService {

    UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    AuthFindService authFindService;

    public UserUpdateService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthFindService authFindService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authFindService = authFindService;
    }

    public UserDTO signUp(UserDTO dto) throws DuplicateMemberException, NotFoundException {

        if(userRepository.existsByEmail(dto.getEmail()))
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

        return userRepository.save(vo).dto(true);

    }

}

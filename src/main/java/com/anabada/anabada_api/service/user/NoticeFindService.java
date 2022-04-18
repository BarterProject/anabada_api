package com.anabada.anabada_api.service.user;

import com.anabada.anabada_api.domain.NoticeVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.user.NoticeDTO;
import com.anabada.anabada_api.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeFindService {

    NoticeRepository noticeRepository;

    UserFindService userFindService;

    @Autowired
    public NoticeFindService(NoticeRepository noticeRepository, UserFindService userFindService) {
        this.noticeRepository = noticeRepository;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public List<NoticeDTO> getMyNotices() throws AuthException {
        UserVO user = userFindService.getMyUserWithAuthorities();
        List<NoticeVO> notices = noticeRepository.getAllByUser(user);

        return notices.stream().map(i -> i.dto(true)).collect(Collectors.toList());
    }
}

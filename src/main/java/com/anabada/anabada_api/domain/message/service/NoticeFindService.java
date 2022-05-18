package com.anabada.anabada_api.domain.message.service;

import com.anabada.anabada_api.domain.message.entity.NoticeVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.message.dto.NoticeDTO;
import com.anabada.anabada_api.domain.message.repository.NoticeRepository;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<NoticeVO> getMyNotices(){
        UserVO user = userFindService.getMyUserWithAuthorities();
        return noticeRepository.getAllByUser(user);
    }
}

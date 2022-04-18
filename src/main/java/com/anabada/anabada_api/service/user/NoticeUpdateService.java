package com.anabada.anabada_api.service.user;

import com.anabada.anabada_api.domain.NoticeVO;
import com.anabada.anabada_api.dto.ResponseDTO;
import com.anabada.anabada_api.dto.user.NoticeDTO;
import com.anabada.anabada_api.repository.NoticeRepository;
import com.anabada.anabada_api.util.HttpRequestUtil;
import com.anabada.anabada_api.util.RequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;


@Service
public class NoticeUpdateService {

    NoticeRepository noticeRepository;
    UserFindService userFindService;
    private final String socketUri;

    public NoticeUpdateService(NoticeRepository noticeRepository, UserFindService userFindService,
                               @Value("${socket.uri}")String socketUri) {
        this.noticeRepository = noticeRepository;
        this.userFindService = userFindService;
        this.socketUri = socketUri;
    }

    @Transactional
    public NoticeVO save(NoticeVO noticeVO) throws URISyntaxException {
        NoticeVO notice = noticeRepository.save(noticeVO);

        //알람 소켓서버로 전달
        HttpRequestUtil<NoticeDTO> requestUtil = new HttpRequestUtil<>();

        RequestEntity requestEntity = new RequestEntity(socketUri);
        requestEntity.addBodyParam("content", notice.getContent())
                .addBodyParam("createdAt", notice.getCreatedAt().toString())
                .addBodyParam("state", notice.getState().toString())
                .addBodyParam("route", notice.getRoute())
                .addBodyParam("kind", notice.getKind())
                .addBodyParam("user", notice.getUser().getEmail());

        ResponseDTO<NoticeDTO> response = requestUtil.post(requestEntity);

        return notice;
    }




}

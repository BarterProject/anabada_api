package com.anabada.anabada_api.domain.message.service;

import com.anabada.anabada_api.domain.message.entity.NoticeVO;
import com.anabada.anabada_api.domain.message.dto.NoticeDTO;
import com.anabada.anabada_api.domain.message.repository.NoticeRepository;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.util.HttpRequestUtil;
import com.anabada.anabada_api.util.RequestEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public NoticeVO save(NoticeVO noticeVO) {
        return noticeRepository.save(noticeVO);

//        //알람 소켓서버로 전달
//        HttpRequestUtil<NoticeDTO> requestUtil = new HttpRequestUtil<>();
//
//        RequestEntity requestEntity = new RequestEntity(socketUri);
//        requestEntity.addBodyParam("content", notice.getContent())
//                .addBodyParam("createdAt", notice.getCreatedAt().toString())
//                .addBodyParam("state", notice.getState().toString())
//                .addBodyParam("route", notice.getRoute())
//                .addBodyParam("kind", notice.getKind())
//                .addBodyParam("user", notice.getUser().getEmail());
//
//        requestUtil.request(requestEntity, HttpMethod.POST);

//        return notice;
    }




}

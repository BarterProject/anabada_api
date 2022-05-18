package com.anabada.anabada_api.firebase;

import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.delivery.service.DeliveryFindService;
import com.anabada.anabada_api.domain.item.entity.DealRequestVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.item.service.DealRequestService;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.message.entity.MessageVO;
import com.anabada.anabada_api.domain.message.entity.NoticeVO;
import com.anabada.anabada_api.domain.message.entity.RoomUserVO;
import com.anabada.anabada_api.domain.message.service.NoticeUpdateService;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FCMService {

    ItemFindService itemFindService;
    NoticeUpdateService noticeUpdateService;
    DealRequestService dealRequestService;
    DeliveryFindService deliveryFindService;

    public FCMService(ItemFindService itemFindService, NoticeUpdateService noticeUpdateService, DealRequestService dealRequestService, DeliveryFindService deliveryFindService) {
        this.itemFindService = itemFindService;
        this.noticeUpdateService = noticeUpdateService;
        this.dealRequestService = dealRequestService;
        this.deliveryFindService = deliveryFindService;
    }

    @Async
    @Transactional
    public void sendItemActivatedNotice(Long itemIdx){
        ItemVO item = itemFindService.findByIdx(itemIdx);

        sendNotification(item.getOwner(),
                "아이템이 활성화 되었습니다.",
                "아이템 " + item.getName() + "이 활성화 되었습니다.",
                "/");
    }

    @Async
    @Transactional
    public void sendRequestSavedNotice(Long requestIdx){
        DealRequestVO request = dealRequestService.findByIdx(requestIdx);

        sendNotification(request.getResponseItem().getOwner(),
                "거래 요청이 도착했습니다.",
                "아이템 " + request.getResponseItem().getName() + "에 거래 요청이 도착했습니다.",
                "/");
    }

    @Async
    @Transactional
    public void sendRequestCompletedNotice(Long requestIdx){
        DealRequestVO request = dealRequestService.findByIdx(requestIdx);

        sendNotification(request.getResponseItem().getOwner(),
                "거래가 성사되었습니다..",
                "아이템 " + request.getRequestItem().getName() + "의 거래가 완료되었습니다.",
                "/");
    }

    @Async
    @Transactional
    public void sendDeliveryRequestedNotice(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        sendNotification(item.getRegistrant(),
                "아이템 배송 요청이 도착하였습니다.",
                "아이템 " + item.getName() + "의 배송 요청이 도착하였습니다.",
                "/");
    }

    @Async
    @Transactional
    public void sendDeliveryStartedNotice(Long deliveryIdx) {
        DeliveryVO delivery = deliveryFindService.findByIdx(deliveryIdx);

        sendNotification(delivery.getItem().getOwner(),
                "아이템 배송이 시작되었습니다.",
                "아이템 " + delivery.getItem().getName() + "의 배송이 시작되었습니다.",
                "/");
    }

    @Async
    @Transactional
    public void sendReturnCompleteNotice(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        sendNotification(item.getRegistrant(),
                "보증금 반환이 완료되었습니다.",
                "아이템 " + item.getName() + "의 보증금 반환이 완료되었습니다.",
                "/");
    }

    @Async
    @Transactional
    public void sendRefundCompleteNotice(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        sendNotification(item.getRegistrant(),
                "보증금 환불이 완료되었습니다.",
                "아이템 " + item.getName() + "의 보증금 환불이 완료되었습니다.",
                "/");
    }


    @Async
    @Transactional
    public void sendNotification(UserVO user, String title, String body, String route) {

        if(user.getFcm() == null)
            return;

        Notification notification = new Notification(title, body);

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(user.getFcm())
                .putData("route", route)
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println(response);

            NoticeVO notice = NoticeVO.builder()
                    .user(user)
                    .content(body)
                    .route(route)
                    .state(1)
                    .title(title)
                    .build();

            noticeUpdateService.save(notice);
        }catch (Exception e){
            log.warn(user.getEmail() + ": 알림 전송에 실패하였습니다.");
        }


    }

    @Async
    @Transactional
    public void sendMessage(MessageVO messageVO, String route){

        List<UserVO> users = messageVO.getRoom().getMappings().stream().map(RoomUserVO::getUser).collect(Collectors.toList());

        MulticastMessage message = MulticastMessage.builder()
                .putData("roomName", messageVO.getRoom().getName())
                .putData("content", messageVO.getContent())
                .putData("route", route)
                .addAllTokens(users.stream().map(UserVO::getFcm).collect(Collectors.toList()))
                .build();

        try{
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            System.out.println(response.getSuccessCount());
        }catch (Exception e){
            log.warn(message.toString() + ": 메시지 전송에 실패하였습니다.");
        }
    }
}

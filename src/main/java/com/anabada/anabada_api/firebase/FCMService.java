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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void sendItemActivatedNotice(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", item.getIdx().toString());

        sendNotification(item.getOwner(),
                "아이템 활성화 알림",
                item.getName() + "아이템이 활성화 되었습니다.",
                "ItemActivated",
                data);
    }

    @Async
    @Transactional
    public void sendRequestSavedNotice(Long requestIdx) {
        DealRequestVO request = dealRequestService.findByIdx(requestIdx);

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", request.getResponseItem().getIdx().toString());

        sendNotification(request.getResponseItem().getOwner(),
                "거래요청 알림",
                request.getResponseItem().getName() + "아이템에 교환 요청되었습니다.",
                "DealBeenRequested",
                data);
    }

    @Async
    @Transactional
    public void sendRequestCompletedNotice(Long requestIdx) {
        DealRequestVO request = dealRequestService.findByIdx(requestIdx);

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", request.getResponseItem().getIdx().toString());

        sendNotification(request.getResponseItem().getOwner(),
                "거래승인 알림",
                "니의 " + request.getRequestItem().getName() + "아이템이 " + request.getResponseItem().getName() + "아이템과 교환되었습니다.",
                "DealCompleted",
                data);
    }

    @Async
    @Transactional
    public void sendDeliveryRequestedNotice(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", item.getIdx().toString());

        sendNotification(item.getRegistrant(),
                "배송 요청 알림",
                item.getName() + "에 배송요청되었습니다. 7주일 안에 배송을 해주세요.",
                "DeliveryRequested",
                data);
    }

    @Async
    @Transactional
    public void sendDeliveryStartedNotice(Long deliveryIdx) {
        DeliveryVO delivery = deliveryFindService.findByIdx(deliveryIdx);

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", delivery.getItem().getIdx().toString());

        sendNotification(delivery.getItem().getOwner(),
                "배송 시작 알림",
                delivery.getItem().getName() + "아이템이 배송시 되었습니다.",
                "DeliveryStarted",
                data);
    }

    @Async
    @Transactional
    public void sendReturnCompleteNotice(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", item.getIdx().toString());

        sendNotification(item.getRegistrant(),
                "보증금 반환 알림",
                "보증금이 반환되었습니다.",
                "DepositReturned",
                data);
    }

    @Async
    @Transactional
    public void sendRefundCompleteNotice(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);

        HashMap<String, String> data = new HashMap<>();
        data.put("itemId", item.getIdx().toString());

        sendNotification(item.getRegistrant(),
                "보증금 환불 알림",
                "보증금 환불이 완료되었습니다.",
                "DepositRefunded",
                data);
    }


    @Async
    @Transactional
    public void sendNotification(UserVO user, String title, String body, String channelId, Map<String, String> data) {

        if (user.getFcm() == null)
            return;

        Notification notification = new Notification(title, body);

        AndroidConfig androidConfig = AndroidConfig.builder()
                .putData("channelId", channelId)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(user.getFcm())
                .setAndroidConfig(androidConfig)
                .putAllData(data)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
//            System.out.println(response);
            NoticeVO notice = NoticeVO.builder()
                    .user(user)
                    .content(body)
                    .route(channelId)
                    .state(1)
                    .title(title)
                    .build();

            noticeUpdateService.save(notice);
        } catch (Exception e) {
            log.warn(user.getEmail() + ": 알림 전송에 실패하였습니다.");
        }


    }

    @Async
    @Transactional
    public void sendMessage(MessageVO messageVO) {

        List<UserVO> users = messageVO.getRoom().getMappings().stream().map(RoomUserVO::getUser).collect(Collectors.toList());

        AndroidConfig androidConfig = AndroidConfig.builder()
                .putData("channelId", "chatting")
                .build();

        Notification notification = new Notification(
                "메시지가 수신되었습니다.",
                messageVO.getContent()
        );

        ItemVO item = itemFindService.findByRoom(messageVO.getRoom());

        MulticastMessage message = MulticastMessage.builder()
                .putData("itemId", item.getIdx().toString())
                .putData("content", messageVO.getContent())
                .putData("sender", messageVO.getSender().getEmail())
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .addAllTokens(users.stream().map(UserVO::getFcm).collect(Collectors.toList()))
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (Exception e) {
            log.warn(message.toString() + ": 메시지 전송에 실패하였습니다.");
        }
    }
}

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
                "????????? ????????? ??????",
                item.getName() + "???????????? ????????? ???????????????.",
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
                "???????????? ??????",
                request.getResponseItem().getName() + "???????????? ?????? ?????????????????????.",
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
                "???????????? ??????",
                "?????? " + request.getRequestItem().getName() + "???????????? " + request.getResponseItem().getName() + "???????????? ?????????????????????.",
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
                "?????? ?????? ??????",
                item.getName() + "??? ???????????????????????????. 7?????? ?????? ????????? ????????????.",
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
                "?????? ?????? ??????",
                delivery.getItem().getName() + "???????????? ????????? ???????????????.",
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
                "????????? ?????? ??????",
                "???????????? ?????????????????????.",
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
                "????????? ?????? ??????",
                "????????? ????????? ?????????????????????.",
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
            log.warn(user.getEmail() + ": ?????? ????????? ?????????????????????.");
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
                "???????????? ?????????????????????.",
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
            log.warn(message.toString() + ": ????????? ????????? ?????????????????????.");
        }
    }
}

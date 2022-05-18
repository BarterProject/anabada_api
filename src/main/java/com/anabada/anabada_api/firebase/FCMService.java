package com.anabada.anabada_api.firebase;

import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FCMService {

    @Autowired
    UserFindService userFindService;

    @Transactional(readOnly = true)
    public void sendNotification(String title, String body) {
        UserVO user = userFindService.getMyUserWithAuthorities();

        Notification notification = new Notification(title, body);
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(user.getFcm())
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Transactional(readOnly = true)
    public void sendMessage(){
        UserVO user = userFindService.getMyUserWithAuthorities();

        Message message = Message.builder()
                .putData("title", "fcm test")
                .putData("body", "fcm test")
                .setToken(user.getFcm())
                .build();

        System.out.println(message);

        try{
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}

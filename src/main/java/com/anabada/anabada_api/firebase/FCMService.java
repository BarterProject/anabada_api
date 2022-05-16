package com.anabada.anabada_api.firebase;

import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FCMService {

    UserFindService userFindService;

    @Transactional(readOnly = true)
    public void sendMessageTest(){
        UserVO user = userFindService.getMyUserWithAuthorities();

        Message message = Message.builder()
                .putData("content", "fcm test")
                .setToken(user.getFcm())
                .build();

        try{
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}

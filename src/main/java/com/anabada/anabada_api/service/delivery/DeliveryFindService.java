package com.anabada.anabada_api.service.delivery;


import com.anabada.anabada_api.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryFindService {
    DeliveryRepository deliveryRepository;

    public DeliveryFindService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }



}

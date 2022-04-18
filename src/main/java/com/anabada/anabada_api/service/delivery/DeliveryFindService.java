package com.anabada.anabada_api.service.delivery;


import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.ResponseDTO;
import com.anabada.anabada_api.dto.delivery.DeliveryDTO;
import com.anabada.anabada_api.dto.delivery.DeliveryTrackingDTO;
import com.anabada.anabada_api.repository.DeliveryRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.util.HttpRequestUtil;
import com.anabada.anabada_api.util.RequestEntity;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;

@Service
public class DeliveryFindService {
    DeliveryRepository deliveryRepository;
    ItemFindService itemFindService;
    private final String deliveryUri;
    private final String deliveryKey;

    public DeliveryFindService(DeliveryRepository deliveryRepository,
                               ItemFindService itemFindService,
                               @Value("${smartTracking.url.search.local}") String deliveryUri,
                                @Value("${smartTracking.client.id}")String deliveryKey){
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.deliveryUri = deliveryUri;
        this.deliveryKey=deliveryKey;
    }


    @Transactional(readOnly = true)
    public DeliveryDTO findByItem(Long itemIdx) throws NotFoundException {
        ItemVO item=itemFindService.findByIdx(itemIdx);
        DeliveryDTO delivery=item.getDelivery().dto(false);
        return delivery;
    }
    @Transactional(readOnly = true)
    public DeliveryTrackingDTO getTracking(Long itemIdx) throws URISyntaxException, NotFoundException {
        ItemVO item=itemFindService.findByIdx(itemIdx);
        DeliveryVO deliveryVO=item.getDelivery();


        HttpRequestUtil<DeliveryTrackingDTO>requestUtil=new HttpRequestUtil<>();

        RequestEntity requestEntity=new RequestEntity(deliveryUri);
        requestEntity.addQueryParam("t_key",deliveryKey);
        requestEntity.addQueryParam("t_code",deliveryVO.getDeliveryCompany().getCode());
        requestEntity.addQueryParam("t_invoice",deliveryVO.getTrackingNumber());


        ResponseDTO<DeliveryTrackingDTO>response=requestUtil.get(requestEntity);
        return response.getResponse();
    }

}


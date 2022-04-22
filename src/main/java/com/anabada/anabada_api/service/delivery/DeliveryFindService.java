package com.anabada.anabada_api.service.delivery;


import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.delivery.DeliveryDTO;
import com.anabada.anabada_api.dto.delivery.DeliveryTrackingDTO;
import com.anabada.anabada_api.repository.DeliveryRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.util.HttpRequestUtil;
import com.anabada.anabada_api.util.RequestEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class DeliveryFindService {
    DeliveryRepository deliveryRepository;
    ItemFindService itemFindService;
    private final String deliveryUri;
    private final String deliveryKey;

    public DeliveryFindService(DeliveryRepository deliveryRepository,
                               ItemFindService itemFindService,
                               @Value("${smartTracking.url.search.local}") String deliveryUri,
                               @Value("${smartTracking.client.id}") String deliveryKey) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.deliveryUri = deliveryUri;
        this.deliveryKey = deliveryKey;
    }


    @Transactional(readOnly = true)
    public DeliveryDTO findByItem(Long itemIdx) throws NotFoundException {
        ItemVO item = itemFindService.findByIdx(itemIdx);
        DeliveryDTO delivery = item.getDelivery().dto(false,true,false);
        return delivery;
    }

    @Transactional(readOnly = true)
    public DeliveryVO findByIdx(Long idx)throws NotFoundException{

        Optional<DeliveryVO>delivery=deliveryRepository.findById(idx);
        if(delivery.isEmpty())
            throw new NotFoundException("invalid");

        return delivery.get();
    }
    @Transactional(readOnly = true)
    public DeliveryTrackingDTO getTracking(Long itemIdx) throws URISyntaxException, NotFoundException {
        ItemVO item = itemFindService.findByIdx(itemIdx);
        DeliveryVO deliveryVO = item.getDelivery();
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpRequestUtil<DeliveryTrackingDTO> requestUtil = new HttpRequestUtil<>();

        RequestEntity requestEntity = new RequestEntity(deliveryUri);
        requestEntity.addQueryParam("t_key", deliveryKey);
        requestEntity.addQueryParam("t_code", deliveryVO.getDeliveryCompany().getCode());
        requestEntity.addQueryParam("t_invoice", deliveryVO.getTrackingNumber());

        LinkedHashMap<String, Object> response = requestUtil.request(requestEntity, HttpMethod.GET);

        return objectMapper.convertValue(response, DeliveryTrackingDTO.class);
    }

}


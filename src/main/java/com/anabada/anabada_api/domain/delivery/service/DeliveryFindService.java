package com.anabada.anabada_api.domain.delivery.service;


import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryDTO;
import com.anabada.anabada_api.domain.delivery.dto.DeliveryTrackingDTO;
import com.anabada.anabada_api.domain.delivery.repository.DeliveryRepository;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
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
    public DeliveryVO findByItem(Long itemIdx) {
        ItemVO item = itemFindService.findByIdx(itemIdx);
        DeliveryVO delivery = item.getDelivery();
        return delivery;
    }

    @Transactional(readOnly = true)
    public DeliveryVO findByIdx(Long idx){

        Optional<DeliveryVO> delivery = deliveryRepository.findById(idx);

        if (delivery.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        return delivery.get();
    }

    @Transactional(readOnly = true)
    public DeliveryTrackingDTO getTracking(Long itemIdx){
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


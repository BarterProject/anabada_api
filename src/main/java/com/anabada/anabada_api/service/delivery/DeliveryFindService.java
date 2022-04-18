package com.anabada.anabada_api.service.delivery;


import com.anabada.anabada_api.domain.delivery.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.delivery.DeliveryDTO;
import com.anabada.anabada_api.dto.delivery.DeliveryTrackingDTO;
import com.anabada.anabada_api.repository.DeliveryRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.util.DeliveryUtil;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryFindService {
    DeliveryRepository deliveryRepository;
    ItemFindService itemFindService;
    DeliveryUtil deliveryUtil;


    public DeliveryFindService(DeliveryRepository deliveryRepository, ItemFindService itemFindService, DeliveryUtil deliveryUtil) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.deliveryUtil = deliveryUtil;
    }

    @Transactional(readOnly = true)
    public DeliveryDTO findByItem(Long itemIdx) throws NotFoundException {
        ItemVO item=itemFindService.findByIdx(itemIdx);
        DeliveryDTO delivery=item.getDelivery().dto(false);
        return delivery;
    }

    @Transactional(readOnly = true)
    public DeliveryTrackingDTO getTracking(Long itemIdx)throws NotFoundException{
        ItemVO item=itemFindService.findByIdx(itemIdx);
        DeliveryVO deliveryVO=item.getDelivery();
        String company=deliveryVO.getDeliveryCompany().getCode();
        String tracking=deliveryVO.getTrackingNumber();
        DeliveryTrackingDTO dto= deliveryUtil.searchDelivery(company,tracking);
        return dto;
    }
}


package com.anabada.anabada_api.service.item;

import com.anabada.anabada_api.domain.item.ItemCategoryVO;
import com.anabada.anabada_api.domain.item.ItemImageVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.dto.payment.PaymentDTO;
import com.anabada.anabada_api.repository.ItemRepository;
import com.anabada.anabada_api.service.payment.PaymentFindService;
import com.anabada.anabada_api.service.payment.PaymentUpdateService;
import com.anabada.anabada_api.service.user.UserFindService;
import com.anabada.anabada_api.util.FileUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemUpdateService {

    ItemRepository itemRepository;

    UserFindService userFindService;
    PaymentUpdateService paymentUpdateService;
    CategoryFindService categoryFindService;
    ItemImageService itemImageService;

    public ItemUpdateService(ItemRepository itemRepository, UserFindService userFindService, PaymentUpdateService paymentUpdateService, CategoryFindService categoryFindService, ItemImageService itemImageService) {
        this.itemRepository = itemRepository;
        this.userFindService = userFindService;
        this.paymentUpdateService = paymentUpdateService;
        this.categoryFindService = categoryFindService;
        this.itemImageService = itemImageService;
    }

    @Transactional
    public ItemVO save(ItemVO item) {
        return itemRepository.save(item);
    }

    @Transactional
    public ItemDTO save(ItemDTO itemDTO, List<MultipartFile> mfList) throws AuthException, NotFoundException, IOException, NotSupportedException {

        UserVO user = userFindService.getMyUserWithAuthorities();
        PaymentVO payment = paymentUpdateService.save(itemDTO.getPayment());
        ItemCategoryVO category = categoryFindService.getByIdx(itemDTO.getItemCategory().getIdx());

        ItemVO item = ItemVO.builder()
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .clauseAgree(itemDTO.isClause_agree())
                .payment(payment)
                .itemCategory(category)
                .deposit(payment.getAmount())
                .owner(user)
                .registrant(user)
                .state(1L)
                .build();

        ItemVO savedItem = itemRepository.save(item);

        Long i = 1L;
        for(MultipartFile mf : mfList){
            ItemImageVO image = itemImageService.save(mf, savedItem, i);
            savedItem.addImage(image);
            i++;
        }

        return savedItem.dto(true, true, true, true, true);

    }


}

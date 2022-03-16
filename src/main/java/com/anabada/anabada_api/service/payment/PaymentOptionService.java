package com.anabada.anabada_api.service.payment;


import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.domain.pay.PaymentVO;
import com.anabada.anabada_api.repository.PaymentOptionRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.openmbean.OpenDataException;
import java.util.Optional;

@Service
public class PaymentOptionService {
    @Autowired
    PaymentOptionRepository paymentOptionRepository;

    public PaymentOptionService(PaymentOptionRepository paymentOptionRepository) {
            this.paymentOptionRepository=paymentOptionRepository;
    }

    @Transactional(readOnly = true)
    public PaymentOptionVO getByName(String name)throws NotFoundException{
        Optional<PaymentOptionVO> option=paymentOptionRepository.findByName(name);

        if(option.isEmpty())
            throw new NotFoundException("invalid option name");

        return option.get();
    }
}

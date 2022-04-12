package com.anabada.anabada_api.util;


import com.anabada.anabada_api.dto.delivery.DeliveryTrackingDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;


@Component
public class DeliveryUtil {
    @Value("${smartTracking.client.id}")
    private String smartTrackingId;

    @Value("${smartTracking.url.search.local}")
    private String smartTrackingUrl;

    public DeliveryTrackingDTO searchDelivery(String t_code,String t_invoice) {
        RestTemplate restTemplate=new RestTemplate();
        URI targetUrl = UriComponentsBuilder
                .fromUriString(smartTrackingUrl)
                .queryParam("t_key",smartTrackingId)
                .queryParam("t_code",t_code)
                .queryParam("t_invoice",t_invoice)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity httpEntity=new HttpEntity<>(headers);

        return restTemplate.exchange(targetUrl,HttpMethod.GET,httpEntity,DeliveryTrackingDTO.class).getBody();
    }


}

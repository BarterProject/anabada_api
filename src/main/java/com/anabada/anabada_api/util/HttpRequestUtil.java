package com.anabada.anabada_api.util;

import com.anabada.anabada_api.dto.ResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class HttpRequestUtil<T> {

    public ResponseDTO<T> post(RequestEntity entity) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        URI uri = UriComponentsBuilder
                .fromUriString(entity.getUri())
                .queryParams(entity.getQueryParams())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(entity.getBodyParams(), headers);

        return restTemplate.exchange(
                uri,
                HttpMethod.POST,
                httpEntity, new ParameterizedTypeReference<ResponseDTO<T>>() {
                }
        ).getBody();
    }

}

package com.anabada.anabada_api.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class RequestEntity {


    private String uri;

    private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    private MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();



    public RequestEntity(String uri) {
        this.uri = uri;
    }

    public RequestEntity addQueryParam(String key, String value){
        queryParams.add(key, value);
        return this;
    }

    public RequestEntity addBodyParam(String key, String value){
        bodyParams.add(key, value);
        return this;
    }

    public String getUri() {
        return uri;
    }

    public MultiValueMap<String, String> getQueryParams() {
        return queryParams;
    }

    public MultiValueMap<String, String> getBodyParams() {
        return bodyParams;
    }
}

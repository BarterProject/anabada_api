package com.anabada.anabada_api.domain.etc.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {

    private T response;

}

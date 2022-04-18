package com.anabada.anabada_api.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {

    private T response;

}

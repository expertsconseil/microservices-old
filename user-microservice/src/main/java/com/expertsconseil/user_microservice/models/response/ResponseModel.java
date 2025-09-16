package com.expertsconseil.user_microservice.models.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ResponseModel <T, S> implements IResponseModel<T> {

    private T data;
    private String message;

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

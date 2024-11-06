package com.huodan.common;

import lombok.Data;

@Data
public class Result<T> {
    private Boolean success;
    private String message;
    private T data;

    public Result(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
} 
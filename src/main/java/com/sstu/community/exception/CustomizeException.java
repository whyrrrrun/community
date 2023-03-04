package com.sstu.community.exception;

public class CustomizeException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomizeException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public CustomizeException(ICustomizeErrorCode customizeErrorCode) {
        message = customizeErrorCode.getMessage();
        code = customizeErrorCode.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}

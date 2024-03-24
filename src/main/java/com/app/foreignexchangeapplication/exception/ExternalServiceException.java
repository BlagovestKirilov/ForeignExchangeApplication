package com.app.foreignexchangeapplication.exception;

public class ExternalServiceException extends RuntimeException{
    private String code;
    private String message;
    public ExternalServiceException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

package com.crepestrips.orderservice.dto;

public class DefaultResult {

    private String message;
    private boolean error;
    private Object result;

    public DefaultResult(String message, boolean error, Object result) {
        this.message = message;
        this.error = error;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    
}

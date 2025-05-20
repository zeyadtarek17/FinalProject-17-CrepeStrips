package com.crepestrips.userservice.dto;

public class ResultDTO<T> {
    private T result;
    private boolean errorOccurred;
    private String message;

    public ResultDTO() {
    }

    public ResultDTO(T result, boolean errorOccurred, String message) {
        this.result = result;
        this.errorOccurred = errorOccurred;
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

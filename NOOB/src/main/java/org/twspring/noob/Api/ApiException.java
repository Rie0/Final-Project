package org.twspring.noob.Api;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
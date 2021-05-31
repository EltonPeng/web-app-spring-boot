package com.zijian.java.web.spring.webapp.exceptions;

public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = 42100L;

    public UserServiceException(String message) {
        super(message);
    }
    
}

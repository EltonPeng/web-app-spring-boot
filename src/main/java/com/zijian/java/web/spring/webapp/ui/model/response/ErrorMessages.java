package com.zijian.java.web.spring.webapp.ui.model.response;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Required field is missing. --"),
    RECORD_ALREADY_EXISTS("Record already exists. --"),
    NO_RECORD_FOUND("Record with provided id is not found. --"),
    INTERNAL_SERVER_ERROR("Internal server error. --"),
    AUTHENTICATION_FAILED("Authentication failed. --");

    private String errorMessage;

    ErrorMessages(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

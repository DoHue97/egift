package com.crm.egift.model;

public enum ResultCode {
    OK("00"),
    INVALID_CARD("01"),
    CUSTOMER_NOT_FOUND("02"),
    CUSTOMER_FINANCIAL_NOT_FOUND("03"),
    UNKNOWN_ERROR("99"),
    NOT_CONNECT_API("404"),
    REQUEST_TIMEOUT("408"),
    MANY_REQUESTS("429");
    private String label;
    ResultCode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

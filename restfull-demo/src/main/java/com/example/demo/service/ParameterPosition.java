package com.example.demo.service;

public enum ParameterPosition {
    INPUT_HEADER("headers"),
    INPUT_PATH("uriParameters"),
    INPUT_URL("queryParameters"),
    INPUT_BODY("body"),
    ERROR_CODE("ERROR_CODE"),
    EXAMPLE_VALUE("EXAMPLE");
    private String value;

    private ParameterPosition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}

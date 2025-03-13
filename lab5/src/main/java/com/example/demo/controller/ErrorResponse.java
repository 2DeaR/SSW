package com.example.demo.controller;

public class ErrorResponse {
    private String error;
    private String message;
    private String path;

    public ErrorResponse(String error, String message, String path) {
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
package com.sparta.api.spartarestapi.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorMessage {
    private String status;
    private String message;
    private String time;

    public ErrorMessage(String status, String message) {
        this.status = status;
        this.message = message;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}

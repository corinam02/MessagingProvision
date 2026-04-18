package com.example.messageprovisioning.provisioning.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ErrorResponse {

    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String correlationId;
    private List<String> fieldErrors;


    public ErrorResponse(LocalDateTime timeStamp, int status, String error, String message, String path, String correlationId, List<String> fieldErrors) {
        this.timeStamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.correlationId = correlationId;
        this.fieldErrors = fieldErrors;
    }
}

package com.example.infinum.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ApiError {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, Set<String>> errors;
}

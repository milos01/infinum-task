package com.example.infinum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> entitity, String field, Object value) {
        super(String.format("%s with %s (%s) can't be found", entitity.getSimpleName(), field, value));
    }

}

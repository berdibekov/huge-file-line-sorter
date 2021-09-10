package com.berdibekov.exceptions;

public class ValidationException extends Exception {

    public ValidationException(String lines_at_converted) {
        super(lines_at_converted);
    }
}

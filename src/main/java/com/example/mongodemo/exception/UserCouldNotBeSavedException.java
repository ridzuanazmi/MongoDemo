package com.example.mongodemo.exception;

public class UserCouldNotBeSavedException extends RuntimeException {
    public UserCouldNotBeSavedException(String message) {
        super(message);
    }
}

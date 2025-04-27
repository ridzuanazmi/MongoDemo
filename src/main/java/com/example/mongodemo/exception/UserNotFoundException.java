package com.example.mongodemo.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) { super(message); }
}

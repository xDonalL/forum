package com.forum.forum.util.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String login) {
        super("User with login '" + login + "' already exists");
    }
}

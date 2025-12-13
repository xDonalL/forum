package com.forum.forum.util.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException() {
        super("Passwords do not match");
    }
}

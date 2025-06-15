package com.task.tracker.exceptions;

import javax.naming.AuthenticationException;

public class InsufficientAuthenticationException extends AuthenticationException {
    public InsufficientAuthenticationException(String s) {
        super(s);
    }
}

package com.library.exception;

public class DuplicateUserException extends LibraryException {
    private static final long serialVersionUID = 1L;
    public DuplicateUserException(String email) { super("An account already exists for " + email + "."); }
}

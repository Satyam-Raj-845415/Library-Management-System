package com.library.exception;

public class InvalidLibraryInputException extends LibraryException {
    private static final long serialVersionUID = 1L;
    public InvalidLibraryInputException(String message) { super(message); }
}

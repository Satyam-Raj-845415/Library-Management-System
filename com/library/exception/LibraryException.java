package com.library.exception;

/** Base class for expected library business-rule failures. */
public class LibraryException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public LibraryException(String message) { super(message); }
}

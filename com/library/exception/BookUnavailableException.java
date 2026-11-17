package com.library.exception;

public class BookUnavailableException extends LibraryException {
    private static final long serialVersionUID = 1L;
    public BookUnavailableException(int bookId) { super("Book " + bookId + " is not currently available."); }
}

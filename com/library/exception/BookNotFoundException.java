package com.library.exception;

public class BookNotFoundException extends LibraryException {
    private static final long serialVersionUID = 1L;
    public BookNotFoundException(int bookId) { super("No book exists with ID " + bookId + "."); }
}

package com.library.exception;

public class BookNotIssuedToUserException extends LibraryException {
    private static final long serialVersionUID = 1L;
    public BookNotIssuedToUserException(int bookId) { super("Book " + bookId + " is not issued to your account."); }
}

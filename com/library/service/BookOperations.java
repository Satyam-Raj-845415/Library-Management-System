package com.library.service;

import com.library.model.Book;
import java.util.List;

// Interface: defines operations available to the presentation layer.
public interface BookOperations {
    List<Book> getAllBooks();
    Book findBook(int bookId);
    Book findBook(String title);
    boolean issueBook(int bookId, String userEmail);
    boolean returnBook(int bookId, String userEmail);
    List<Book> getPendingBooks(String userEmail);
    void openBookUrl(int bookId);
}

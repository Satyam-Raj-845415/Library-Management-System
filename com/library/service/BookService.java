package com.library.service;

import com.library.exception.BookNotFoundException;
import com.library.exception.BookNotIssuedToUserException;
import com.library.exception.BookUnavailableException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;

public class BookService implements BookOperations {
    private final BookRepository bookRepo;

    public BookService(BookRepository bookRepo) { this.bookRepo = bookRepo; }

    @Override
    public List<Book> getAllBooks() { return bookRepo.getAllBooks(); }

    @Override
    public Book findBook(int bookId) { return bookRepo.findById(bookId); }

    @Override
    public Book findBook(String title) {
        if (title == null || title.trim().isEmpty()) return null;
        for (Book book : bookRepo.getAllBooks()) {
            if (book.getTitle().equalsIgnoreCase(title.trim())) return book;
        }
        return null;
    }

    /** Synchronization makes the check-and-update operation atomic across threads. */
    @Override
    public synchronized boolean issueBook(int bookId, String userEmail) {
        Book book = findBook(bookId);
        if (book == null) throw new BookNotFoundException(bookId);
        if (!"Available".equals(book.getStatus())) throw new BookUnavailableException(bookId);
        book.setStatus("Issued");
        book.setIssuedTo(userEmail);
        bookRepo.updateBook(book);
        return true;
    }

    @Override
    public synchronized boolean returnBook(int bookId, String userEmail) {
        Book book = findBook(bookId);
        if (book == null) throw new BookNotFoundException(bookId);
        if (!"Issued".equals(book.getStatus()) || !userEmail.equals(book.getIssuedTo())) {
            throw new BookNotIssuedToUserException(bookId);
        }
        book.setStatus("Available");
        book.setIssuedTo("");
        bookRepo.updateBook(book);
        return true;
    }

    @Override
    public List<Book> getPendingBooks(String userEmail) { return bookRepo.getIssuedBooksByEmail(userEmail); }

    @Override
    public void openBookUrl(int bookId) {
        Book book = findBook(bookId);
        if (book == null) throw new BookNotFoundException(bookId);
        if (!Desktop.isDesktopSupported()) {
            throw new IllegalStateException("Opening a browser is not supported on this computer.");
        }
        try {
            Desktop.getDesktop().browse(new URI(book.getUrl()));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to open the book URL: " + e.getMessage(), e);
        }
    }
}

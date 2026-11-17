package com.library;

import com.library.cli.LibraryCLI;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.service.AuthService;
import com.library.service.BackgroundTaskManager;
import com.library.service.BookOperations;
import com.library.service.BookService;
import com.library.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        BookRepository bookRepo = new BookRepository();

        AuthService authService = new AuthService(userRepo);
        BookOperations bookService = new BookService(bookRepo);
        UserService userService = new UserService(userRepo);
        BackgroundTaskManager taskManager = new BackgroundTaskManager();

        LibraryCLI cli = new LibraryCLI(authService, bookService, userService, taskManager);
        System.out.println("Initializing Library System...");
        try {
            cli.start();
        } finally {
            taskManager.close();
        }
    }
}

package com.library.cli;

import com.library.exception.LibraryException;
import com.library.model.Book;
import com.library.model.User;
import com.library.service.AuthService;
import com.library.service.BackgroundTaskManager;
import com.library.service.BookOperations;
import com.library.service.UserService;
import java.util.List;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LibraryCLI {
    private final AuthService authService;
    private final BookOperations bookService;
    private final UserService userService;
    private final BackgroundTaskManager taskManager;
    private final Scanner scanner;
    private boolean running = true;

    public LibraryCLI(AuthService authService, BookOperations bookService, UserService userService,
                      BackgroundTaskManager taskManager) {
        this.authService = authService;
        this.bookService = bookService;
        this.userService = userService;
        this.taskManager = taskManager;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (running) {
            try {
                if (!authService.isLoggedIn()) showAuthMenu();
                else showDashboardMenu();
            } catch (LibraryException | IllegalStateException e) {
                System.out.println("Operation failed: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("No more console input. Closing Library System.");
                running = false;
            }
        }
    }

    private void showAuthMenu() {
        System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===\n1. Login\n2. Register\n3. Exit");
        System.out.print("Choose an option: ");
        switch (scanner.nextLine().trim()) {
            case "1": handleLogin(); break;
            case "2": handleRegister(); break;
            case "3": running = false; System.out.println("Exiting Library System."); break;
            default: System.out.println("Invalid option. Please choose 1, 2, or 3.");
        }
    }

    private void handleLogin() {
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Password: "); String password = scanner.nextLine();
        if (authService.login(email, password)) {
            System.out.println("Login successful! Welcome " + authService.getCurrentUser().getName());
        } else System.out.println("Invalid email or password.");
    }

    private void handleRegister() {
        System.out.print("Full Name: "); String name = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Student ID: "); String sid = scanner.nextLine();
        System.out.print("Course: "); String course = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        User newUser = new User(name, email.trim(), pass, sid, course);
        authService.register(newUser);
        System.out.println("Registration successful! Please login.");
    }

    private void showDashboardMenu() {
        User current = authService.getCurrentUser();
        System.out.println("\n=== DASHBOARD (Welcome, " + current.getName() + ") ===");
        System.out.println("1. View All Books\n2. Issue a Book\n3. Return a Book\n4. View Pending Books"
                + "\n5. Edit Profile\n6. Logout\n7. Search Book by Title\n8. Generate Inventory Report (Background Thread)");
        System.out.print("Choose an option: ");
        switch (scanner.nextLine().trim()) {
            case "1": viewAllBooks(); break;
            case "2": issueBook(); break;
            case "3": returnBook(); break;
            case "4": viewPendingBooks(); break;
            case "5": editProfile(); break;
            case "6": authService.logout(); System.out.println("Logged out."); break;
            case "7": searchBookByTitle(); break;
            case "8": generateInventoryReport(); break;
            default: System.out.println("Invalid option.");
        }
    }

    private void viewAllBooks() {
        System.out.println("\n--- ALL BOOKS ---");
        System.out.println("Total book objects created: " + Book.getTotalBooksCreated());
        System.out.printf("%-5s %-30s %-20s %-20s %-10s%n", "ID", "Title", "Author", "Category", "Status");
        for (Book b : bookService.getAllBooks()) {
            System.out.printf("%-5d %-30s %-20s %-20s %-10s%n", b.getId(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getStatus());
        }
    }

    private void searchBookByTitle() {
        System.out.print("Enter exact book title: ");
        Book book = bookService.findBook(scanner.nextLine());
        if (book == null) System.out.println("Book not found.");
        else System.out.println("Found: " + book.getTitle() + " by " + book.getAuthor() + " | Status: " + book.getStatus());
    }

    private void issueBook() {
        viewAllBooks();
        System.out.print("Enter Book ID to issue: ");
        Integer id = readBookId();
        if (id == null) return;
        bookService.issueBook(id, authService.getCurrentUser().getEmail());
        System.out.println("Book issued successfully!");
    }

    private void returnBook() {
        List<Book> pending = bookService.getPendingBooks(authService.getCurrentUser().getEmail());
        if (pending.isEmpty()) { System.out.println("You have no issued books."); return; }
        System.out.println("\n--- YOUR ISSUED BOOKS ---");
        for (Book b : pending) System.out.println(b.getId() + ": " + b.getTitle());
        System.out.print("Enter Book ID to return: ");
        Integer id = readBookId();
        if (id == null) return;
        bookService.returnBook(id, authService.getCurrentUser().getEmail());
        System.out.println("Book returned successfully!");
    }

    private Integer readBookId() {
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (id <= 0) { System.out.println("Book ID must be a positive number."); return null; }
            return id;
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Enter a whole number.");
            return null;
        }
    }

    private void viewPendingBooks() {
        List<Book> pending = bookService.getPendingBooks(authService.getCurrentUser().getEmail());
        if (pending.isEmpty()) { System.out.println("No pending books."); return; }
        System.out.println("\n--- PENDING BOOKS ---");
        for (Book b : pending) System.out.println(b.getId() + ": " + b.getTitle() + " | URL: " + b.getUrl());
        System.out.print("Enter Book ID to open URL (or 0 to cancel): ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (id > 0) bookService.openBookUrl(id);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void editProfile() {
        User current = authService.getCurrentUser();
        String oldEmail = current.getEmail();
        System.out.println("Leave blank to keep current value.");
        System.out.print("Name [" + current.getName() + "]: "); String name = scanner.nextLine();
        System.out.print("Email [" + current.getEmail() + "]: "); String email = scanner.nextLine();
        System.out.print("Student ID [" + current.getStudentId() + "]: "); String sid = scanner.nextLine();
        System.out.print("Course [" + current.getCourse() + "]: "); String course = scanner.nextLine();
        System.out.print("Password [hidden]: "); String pass = scanner.nextLine();
        if (!name.isEmpty()) current.setName(name);
        if (!email.isEmpty()) current.setEmail(email.trim());
        if (!sid.isEmpty()) current.setStudentId(sid);
        if (!course.isEmpty()) current.setCourse(course);
        if (!pass.isEmpty()) current.setPassword(pass);
        userService.updateProfile(oldEmail, current);
        System.out.println("Profile updated successfully.");
    }

    private void generateInventoryReport() {
        System.out.println("Submitting inventory report to a background worker...");
        Future<String> report = taskManager.generateInventoryReport(bookService);
        try {
            System.out.println(report.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Report wait was interrupted. Please try again.");
        } catch (ExecutionException e) {
            System.out.println("Report task failed: " + e.getCause().getMessage());
        }
    }
}

# Library Management System

A console-based **Library Management System** written in **Java**. Students can register, log in, browse and search a catalogue of books, issue and return them, and keep track of what they currently have borrowed. 


## Features

- **Student accounts**: register a new account or log in to an existing one
- **Browse books**: view the full catalogue with ID, title, author, category, and availability status
- **Search**: find a book by its exact title (case-insensitive)
- **Issue a book**: borrow any available book; the system prevents two people from holding the same copy
- **Return a book**: return only the books that are issued to *your* account
- **Pending books**: see everything you currently have issued and open its reference link in your web browser
- **Edit profile**: update your name, email, student ID, course, or password (leave a field blank to keep the current value)
- **Inventory report**: generate a summary (total / available / issued) on a **background worker thread**
- **Friendly error messages**: expected problems (unknown book, book already issued, duplicate email, bad input) are reported clearly instead of crashing the program


## Concepts Demonstrated

| Concept | Where it appears |
|---|---|
| **Inheritance** | `User` extends the abstract `Person` class |
| **Abstraction** | `Person` declares an abstract `getRole()` method; `BookOperations` is an interface that hides implementation details |
| **Polymorphism** | `User` overrides `getRole()` (runtime); `BookService.findBook(int)` and `findBook(String)` are overloaded (compile-time) |
| **Encapsulation** | Private fields with getters/setters in `Book`, `User`, and `Person` |
| **Static members** | `Book.totalBooksCreated` counts every `Book` object created |
| **Programming to an interface** | `LibraryCLI` talks to `BookOperations`, not to the concrete `BookService` |
| **Dependency injection** | `Main` creates the repositories and services and passes them in through constructors |
| **Layered architecture** | Separate packages for CLI, services, repositories, models, and exceptions |
| **Custom exceptions** | A `LibraryException` hierarchy for business-rule failures |
| **Collections & Streams** | `ArrayList`, `HashMap`, and stream filtering in the repositories and report task |
| **Multithreading** | `ExecutorService` thread pool, `Callable`, and `Future` in `BackgroundTaskManager` |
| **Synchronization** | `synchronized` methods protect shared in-memory data and make issue/return atomic |

---

## Architecture

The application is split into layers. Each layer only talks to the layer directly below it.

```
┌──────────────────────────────────────────────┐
│  CLI layer          LibraryCLI               │  menus, input, output
└──────────────────────┬───────────────────────┘
                       │
┌──────────────────────▼───────────────────────┐
│  Service layer      AuthService              │  business rules
│                     BookService              │
│                       (implements            │
│                        BookOperations)       │
│                     UserService              │
│                     BackgroundTaskManager    │
└──────────────────────┬───────────────────────┘
                       │
┌──────────────────────▼───────────────────────┐
│  Repository layer   BookRepository           │  in-memory storage
│                     UserRepository           │
└──────────────────────┬───────────────────────┘
                       │
┌──────────────────────▼───────────────────────┐
│  Model layer        Person ◄── User          │  data objects
│                     Book                     │
└──────────────────────────────────────────────┘

---

## Project Structure

```
library_oops_updated/
├── README.md
└── com/library/
    ├── Main.java                         # Application entry point
    ├── cli/
    │   └── LibraryCLI.java               # Console menus and user interaction
    ├── service/
    │   ├── AuthService.java              # Login, logout, registration, current session
    │   ├── BookOperations.java           # Interface for book-related operations
    │   ├── BookService.java              # Issue / return / search logic
    │   ├── UserService.java              # Profile updates
    │   └── BackgroundTaskManager.java    # Thread pool for background tasks
    ├── repository/
    │   ├── BookRepository.java           # In-memory book storage (ArrayList)
    │   └── UserRepository.java           # In-memory user storage (HashMap)
    ├── model/
    │   ├── Person.java                   # Abstract parent class
    │   ├── User.java                     # Student account (extends Person)
    │   └── Book.java                     # Book record
    └── exception/
        ├── LibraryException.java         # Base unchecked exception
        ├── BookNotFoundException.java
        ├── BookUnavailableException.java
        ├── BookNotIssuedToUserException.java
        ├── DuplicateUserException.java
        └── InvalidLibraryInputException.java
```


### Demo account

| Field | Value |
|---|---|
| Email | `student@gmail.com` |
| Password | `12345` |

You can also choose **Register** from the first menu to create your own account.

---


Initializing Library System...

=== LIBRARY MANAGEMENT SYSTEM ===
1. Login
2. Register
3. Exit
Choose an option: 1
Email: student@gmail.com
Password: 12345
Login successful! Welcome Demo Student

Choose an option: 2
Enter Book ID to issue: 3
Book issued successfully!

Choose an option: 2
Enter Book ID to issue: 3
Operation failed: Book 3 is not currently available.

Choose an option: 8
Submitting inventory report to a background worker...
Inventory report generated by library-worker-1
Total books: 17
Available: 16
Issued: 1

Choose an option: 7
Enter exact book title: Clean Code
Found: Clean Code by Robert C. Martin | Status: Available

Choose an option: 3
--- YOUR ISSUED BOOKS ---
3: Data Structures and Algorithms
Enter Book ID to return: 3
Book returned successfully!

Choose an option: 6
Logged out.
```

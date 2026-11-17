<<<<<<< HEAD
# Library Management System 

A console-based Java Library Management System using a layered package structure.

## Features
- Student registration and login
- View and search books
- Issue and return books
- View issued/pending books and open their reference URLs
- Edit student profile
- Generate an inventory report on a background worker thread
- Custom exceptions for expected business-rule failures
- Synchronized book operations and repository access for shared in-memory data

## Package structure
- `com.library` — application entry point (`Main`)
- `com.library.cli` — console menus and user interaction (`LibraryCLI`)
- `com.library.service` — business logic, interface, and background task manager
- `com.library.repository` — in-memory book and user storage
- `com.library.model` — `Person`, `User`, and `Book`
- `com.library.exception` — custom library exceptions

## Requirements
JDK 8 or later.

## Compile
From the directory containing the `com` folder:

```bash
javac -d out $(find com -name "*.java")
```

## Run

```bash
java -cp out com.library.Main
```

Demo account:
- Email: `student@gmail.com`
- Password: `12345`

After login, choose option `8` to generate the inventory report using `ExecutorService` on a worker thread.

## Multithreading design
`BackgroundTaskManager` owns a fixed thread pool and submits the inventory-report task as a `Callable<String>`. The task runs on a named `library-worker-*` thread and returns its report through a `Future<String>`. `Main` closes the task manager when the CLI exits. `BookService` synchronizes the issue/return check-and-update operations, while repository methods synchronize access to the in-memory collections.

The console currently waits for the submitted report using `Future.get()` so it can display the completed result. The work itself executes on a worker thread; this is a simple, observable introduction to Java concurrency without introducing concurrent console input/output.

## Exception handling design
- `LibraryException` is the base unchecked exception for expected library-domain failures.
- `BookNotFoundException`, `BookUnavailableException`, `BookNotIssuedToUserException`, `DuplicateUserException`, and `InvalidLibraryInputException` communicate specific failures.
- `LibraryCLI` catches domain exceptions at the user-interface boundary and displays understandable messages.
- Invalid numeric IDs are handled separately with `NumberFormatException`.
- Background task failures and interruption are handled with `ExecutionException` and `InterruptedException`.

## Important implementation note
This project stores data in memory (`ArrayList` and `HashMap`). Data is reset when the application exits. Passwords are stored as plain text in this educational project; a real deployment should use password hashing and persistent database storage.
=======
# Library-Management-System
This the Project file of Library Management System.
>>>>>>> 72d729ea35343e8586efc4ef1f87ee8662384fb2

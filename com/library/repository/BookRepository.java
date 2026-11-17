// File: com/library/repository/BookRepository.java
package com.library.repository;

import com.library.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository {
    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        loadInitialBooks();
    }

    private void loadInitialBooks() {
        books.add(new Book(1, "Java Programming", "Herbert Schildt", "Programming", "Available", "", "https://docs.oracle.com/javase/tutorial/"));
        books.add(new Book(2, "EGO", "puneet", "MIND", "Available", "", "https://thereadershub.org/wp-content/uploads/2020/07/Ryan_Holiday_Ego_is_the_Enenmyz-lib.org_.pdf"));
        books.add(new Book(3, "Data Structures and Algorithms", "Robert Lafore", "Computer Science", "Available", "", "https://www.geeksforgeeks.org/data-structures/"));
        books.add(new Book(4, "Database Management Systems", "Raghu Ramakrishnan", "Database", "Available", "", "https://www.geeksforgeeks.org/dbms/"));
        books.add(new Book(5, "Computer Networks", "Andrew S. Tanenbaum", "Networking", "Available", "", "https://www.geeksforgeeks.org/computer-network-tutorials/"));
        books.add(new Book(6, "Operating System Concepts", "Abraham Silberschatz", "Operating System", "Available", "", "https://www.geeksforgeeks.org/operating-systems/"));
        books.add(new Book(7, "Artificial Intelligence", "Stuart Russell", "Artificial Intelligence", "Available", "", "https://www.ibm.com/think/topics/artificial-intelligence"));
        books.add(new Book(8, "Python Crash Course", "Eric Matthes", "Programming", "Available", "", "https://docs.python.org/3/tutorial/"));
        books.add(new Book(9, "Clean Code", "Robert C. Martin", "Programming", "Available", "", "https://www.oreilly.com/library/view/clean-code-a/9780136083238/"));
        books.add(new Book(10, "Computer Organization", "Carl Hamacher", "Computer Science", "Available", "", "https://www.geeksforgeeks.org/computer-organization-and-architecture/"));
        books.add(new Book(11, "Software Engineering", "Ian Sommerville", "Software Engineering", "Available", "", "https://www.geeksforgeeks.org/software-engineering/"));
        books.add(new Book(12, "Web Development", "Jon Duckett", "Web Development", "Available", "", "https://developer.mozilla.org/en-US/docs/Learn"));
        books.add(new Book(13, "Machine Learning", "Tom M. Mitchell", "Machine Learning", "Available", "", "https://www.geeksforgeeks.org/machine-learning/"));
        books.add(new Book(14, "C++ Programming", "Bjarne Stroustrup", "Programming", "Available", "", "https://cplusplus.com/doc/tutorial/"));
        books.add(new Book(15, "Cyber Security", "William Stallings", "Cyber Security", "Available", "", "https://www.geeksforgeeks.org/cyber-security-tutorial/"));
        books.add(new Book(16, "Cloud Computing", "Rajkumar Buyya", "Cloud", "Available", "", "https://www.geeksforgeeks.org/cloud-computing/"));
        books.add(new Book(17, "Computer Graphics", "Donald Hearn", "Graphics", "Available", "", "https://www.geeksforgeeks.org/computer-graphics/"));
    }

    public synchronized List<Book> getAllBooks() { return new ArrayList<>(books); }

    public synchronized Book findById(int id) {
        return books.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    public synchronized void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == updatedBook.getId()) {
                books.set(i, updatedBook);
                return;
            }
        }
        throw new IllegalArgumentException("Cannot update a book that is not in the repository.");
    }

    public synchronized List<Book> getIssuedBooksByEmail(String email) {
        return books.stream()
                .filter(b -> "Issued".equals(b.getStatus()) && email.equals(b.getIssuedTo()))
                .collect(Collectors.toList());
    }
}

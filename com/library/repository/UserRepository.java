package com.library.repository;

import com.library.model.User;
import java.util.HashMap;
import java.util.Map;

/** In-memory user storage. Synchronized methods protect shared state. */
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public UserRepository() {
        users.put("student@gmail.com", new User("Demo Student", "student@gmail.com", "12345", "IT001", "B.Tech IT"));
    }

    public synchronized void addUser(User user) { users.put(user.getEmail(), user); }
    public synchronized User findByEmail(String email) { return users.get(email); }
    public synchronized boolean existsByEmail(String email) { return users.containsKey(email); }

    public synchronized void updateUser(String oldEmail, User updatedUser) {
        if (!oldEmail.equals(updatedUser.getEmail()) && users.containsKey(updatedUser.getEmail())) {
            throw new IllegalArgumentException("The updated email is already registered.");
        }
        users.remove(oldEmail);
        users.put(updatedUser.getEmail(), updatedUser);
    }
}

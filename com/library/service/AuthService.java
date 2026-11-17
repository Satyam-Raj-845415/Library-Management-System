package com.library.service;

import com.library.exception.DuplicateUserException;
import com.library.exception.InvalidLibraryInputException;
import com.library.model.User;
import com.library.repository.UserRepository;

public class AuthService {
    private final UserRepository userRepo;
    private User currentUser;

    public AuthService(UserRepository userRepo) { this.userRepo = userRepo; }

    public synchronized boolean login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidLibraryInputException("Email and password are required.");
        }
        User user = userRepo.findByEmail(email.trim());
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(User user) {
        if (user == null || user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new InvalidLibraryInputException("A valid user email is required.");
        }
        if (userRepo.existsByEmail(user.getEmail())) throw new DuplicateUserException(user.getEmail());
        userRepo.addUser(user);
        return true;
    }

    public synchronized void logout() { currentUser = null; }
    public synchronized User getCurrentUser() { return currentUser; }
    public synchronized boolean isLoggedIn() { return currentUser != null; }
}

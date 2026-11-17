package com.library.service;

import com.library.exception.InvalidLibraryInputException;
import com.library.model.User;
import com.library.repository.UserRepository;

public class UserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepo) { this.userRepo = userRepo; }

    public void updateProfile(String oldEmail, User updatedUser) {
        if (updatedUser == null || updatedUser.getEmail() == null || updatedUser.getEmail().trim().isEmpty()) {
            throw new InvalidLibraryInputException("Profile email cannot be empty.");
        }
        try {
            userRepo.updateUser(oldEmail, updatedUser);
        } catch (IllegalArgumentException e) {
            throw new InvalidLibraryInputException(e.getMessage());
        }
    }
}

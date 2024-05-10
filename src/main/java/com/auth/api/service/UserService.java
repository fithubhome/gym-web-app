package com.auth.api.service;

import com.auth.api.exceptions.DuplicateUserException;
import com.auth.api.exceptions.UserNotFoundExceptionToDeleteException;
import com.auth.api.exceptions.UserNotFoundToUpdateException;
import com.auth.api.model.User;
import com.auth.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User addNewUser(User newUser) throws DuplicateUserException {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicateUserException(newUser.getEmail());
        }
        return userRepository.save(newUser);
    }

    public void updateUser(User updatedUser) throws UserNotFoundToUpdateException {
        if (!userRepository.existsById(updatedUser.getId())) {
            throw new UserNotFoundToUpdateException(updatedUser.getId());
        }
        userRepository.save(updatedUser);
    }

    public void deleteUser(UUID id) throws UserNotFoundExceptionToDeleteException {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundExceptionToDeleteException(id);
        }
        userRepository.deleteById(id);
    }
}

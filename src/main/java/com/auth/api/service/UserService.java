package com.auth.api.service;

import com.auth.api.exceptions.DuplicateUserException;
import com.auth.api.exceptions.UserNotFoundExceptionToDeleteException;
import com.auth.api.exceptions.UserNotFoundToUpdateException;
import com.auth.api.model.User;
import com.auth.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
    public User getUserById(int id) {
        return userRepository.findUserByID(id);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User addUser(User newUser) throws DuplicateUserException {
        return userRepository.addUser(newUser);
    }
    public void updateUser(User updatedUser) throws UserNotFoundToUpdateException {
        userRepository.updateUser(updatedUser);
    }
    public void deleteUser(int id) throws UserNotFoundExceptionToDeleteException {
        userRepository.deleteUserById(id);
    }

}

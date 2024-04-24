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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findUserByID(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User addUser(User newUser) throws DuplicateUserException {
        return userRepository.addUser(newUser);
    }

    @Override
    public void updateUser(User updatedUser) throws UserNotFoundToUpdateException {
        userRepository.updateUser(updatedUser);
    }

    @Override
    public void deleteUser(int id) throws UserNotFoundExceptionToDeleteException {
        userRepository.deleteUserById(id);
    }

}

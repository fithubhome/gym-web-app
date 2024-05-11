package com.auth.api.service;

import com.auth.api.exceptions.DuplicateUserException;
import com.auth.api.model.User;
import com.auth.api.model.Profile;
import com.auth.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RoleService roleService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User addUser(User newUser) throws DuplicateUserException {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicateUserException(newUser.getEmail());
        }
        User savedUser = userRepository.save(newUser);
        Profile profile = new Profile(UUID.randomUUID(), savedUser.getId());
        profileService.createProfile(profile);
        roleService.assignRoleToUser(savedUser.getId(), "Member");
        return savedUser;
    }
}

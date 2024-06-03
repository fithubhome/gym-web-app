package com.gym_app.api.service;

import com.gym_app.api.exceptions.DuplicateUserException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.model.Profile;
import com.gym_app.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    public UserEntity findUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public void registerNewUser(UserEntity registerUser) throws DuplicateUserException {
        if (userRepository.findByEmail(registerUser.getEmail()).isPresent()) {
            throw new DuplicateUserException(registerUser.getEmail());
        }
        if (registerUser.getEmail() == null || registerUser.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        userRepository.save(registerUser);

        Profile profile = new Profile(UUID.randomUUID(), registerUser.getId());
        profileService.createProfile(profile);
        roleService.assignRoleToUser(registerUser.getId(), "MEMBER");
    }
    public boolean validateUserCredentials(String email, String rawPassword) {
        UserEntity user = findByEmail(email);
        System.out.println("User from validate credentials: " + user);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}

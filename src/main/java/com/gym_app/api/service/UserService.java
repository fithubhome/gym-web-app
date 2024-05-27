package com.gym_app.api.service;

import com.gym_app.api.exceptions.DuplicateUserException;
import com.gym_app.api.exceptions.RoleNotFoundException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.model.Profile;
import com.gym_app.api.repository.UserRepository;
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

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity findUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserEntity addUser(UserEntity newUserEntity) throws DuplicateUserException, RoleNotFoundException {
        if (userRepository.findByEmail(newUserEntity.getEmail()).isPresent()) {
            throw new DuplicateUserException(newUserEntity.getEmail());
        }
        // Ensure username is set
        if (newUserEntity.getEmail() == null || newUserEntity.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        UserEntity savedUserEntity = userRepository.save(newUserEntity);
        Profile profile = new Profile(UUID.randomUUID(), savedUserEntity.getId());
        profileService.createProfile(profile);
        roleService.assignRoleToUser(savedUserEntity.getId(), "MEMBER");
        return savedUserEntity;
    }
}

package com.bodystats.api.service;

import com.bodystats.api.exceptions.DuplicateUserException;
import com.bodystats.api.model.Profile;
import com.bodystats.api.model.UserEntity;
import com.bodystats.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        if (registerUser.getEmail().contains("admin")){
            roleService.assignRoleToUser(registerUser.getId(), "ADMIN");
        }else {
            roleService.assignRoleToUser(registerUser.getId(), "MEMBER");
        }
    }

    public Optional<UserEntity> findById(UUID userId) {
        return userRepository.findById(userId);
    }
}

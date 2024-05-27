package com.gym_app.api.service;

import com.gym_app.api.exceptions.RoleAlreadyAssignedException;
import com.gym_app.api.exceptions.RoleNotFoundException;
import com.gym_app.api.model.Role;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.repository.RoleRepository;
import com.gym_app.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void assignRoleToUser(UUID userID, String roleType) throws RoleAlreadyAssignedException, RoleNotFoundException {
        UserEntity user = userRepository.findById(userID)
                .orElseThrow(() -> new RoleNotFoundException("User not found for ID: " + userID));

        Optional<Role> roleOptional = roleRepository.findByName(roleType);
        Role role;
        if (roleOptional.isPresent()) {
            role = roleOptional.get();
        } else {
            // Create a new role if it doesn't exist
            role = Role.builder().id(UUID.randomUUID()).name(roleType).build();
            roleRepository.save(role);
        }

        if (user.getRoles().contains(role)) {
            throw new RoleAlreadyAssignedException("Role '" + roleType + "' is already assigned to user ID: " + userID);
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void removeRoleFromUser(UUID userID, String roleType) throws RoleNotFoundException {
        UserEntity user = userRepository.findById(userID)
                .orElseThrow(() -> new RoleNotFoundException("User not found for ID: " + userID));

        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new RoleNotFoundException("Role '" + roleType + "' not found"));

        if (!user.getRoles().contains(role)) {
            throw new RoleNotFoundException("Role '" + roleType + "' not assigned to user ID: " + userID);
        }

        user.getRoles().remove(role);
        userRepository.save(user);
    }

}

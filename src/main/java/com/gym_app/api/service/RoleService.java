package com.gym_app.api.service;

import com.gym_app.api.exceptions.RoleNotFoundException;
import com.gym_app.api.model.Role;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.repository.RoleRepository;
import com.gym_app.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void assignRoleToUser(UUID userId, String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) {
            Role newRole = new Role();
            newRole.setName(roleName);
            roleRepository.save(newRole);
            roleOpt = Optional.of(newRole);
        }

        Role role = roleOpt.get();
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found in RoleService>AssignRoleToUser method."));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void removeRoleFromUser(UUID userId, String roleType) throws RoleNotFoundException {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RoleNotFoundException("User not found for ID: " + userId));

        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new RoleNotFoundException("Role '" + roleType + "' not found"));

        if (!user.getRoles().contains(role)) {
            throw new RoleNotFoundException("Role '" + roleType + "' not assigned to user ID: " + userId);
        }

        user.getRoles().remove(role);
        userRepository.save(user);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}

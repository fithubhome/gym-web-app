package com.gym_app.api.service;

import com.gym_app.api.exceptions.RoleAlreadyAssignedException;
import com.gym_app.api.exceptions.RoleNotFoundException;
import com.gym_app.api.model.Role;
import com.gym_app.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<String> getRoleByUserId(UUID userID) throws RoleNotFoundException {
        List<Role> roles = roleRepository.findByUserId(userID);
        if (roles.isEmpty()) {
            return Collections.singletonList("No roles assigned.");
        }
        return roles.stream().map(Role::getRoleType).collect(Collectors.toList());
    }

    public List<String> getAllRoles() {
        return Arrays.asList("Admin", "Trainer", "Member", "Staff");
    }

    @Transactional
    public void assignRoleToUser(UUID userID, String roleType) throws RoleAlreadyAssignedException {
        if (roleRepository.existsByUserIdAndRoleType(userID, roleType)) {
            throw new RoleAlreadyAssignedException("Role '" + roleType + "' is already assigned.");
        }
        Role role = new Role(UUID.randomUUID(), userID, roleType);
        roleRepository.save(role);
    }

    @Transactional
    public void removeRoleFromUser(UUID userID, String roleType) throws RoleNotFoundException {
        if (!roleRepository.existsByUserIdAndRoleType(userID, roleType)) {
            throw new RoleNotFoundException("Role '" + roleType + "' not found for user ID: " + userID);
        }
        roleRepository.deleteByUserIdAndRoleType(userID, roleType);
    }
}

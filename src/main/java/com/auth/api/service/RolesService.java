package com.auth.api.service;

import com.auth.api.repository.RolesRepository;
import com.auth.api.exceptions.RoleAlreadyAssignedException;
import com.auth.api.exceptions.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UserService userService;

    public List<String> getRoleByUserId(UUID userID) throws RoleNotFoundException {
        return rolesRepository.getRolesByUserID(userID);
    }

    public List<String> getAllRoles() {
        return Arrays.asList("Admin", "Trainer", "Member", "Staff");
    }

    public void assignRoleToUser(UUID userID, String roleType) throws RoleAlreadyAssignedException {
        List<String> existingRoles = rolesRepository.getRolesByUserID(userID);
        if (existingRoles.contains(roleType)) {
            throw new RoleAlreadyAssignedException("Role '" + roleType + "' is already assigned.");
        }
        rolesRepository.assignRoleToUser(userID, roleType);
    }

    public void removeRoleFromUser(UUID userID, String roleType) throws RoleNotFoundException {
        boolean removed = rolesRepository.removeRoleFromUser(userID, roleType);
        if (!removed) {
            throw new RoleNotFoundException("Role '" + roleType + "' not found.");
        }
    }
}


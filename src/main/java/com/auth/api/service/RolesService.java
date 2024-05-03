package com.auth.api.service;

import com.auth.api.model.Role;
import com.auth.api.repository.RolesRepository;
import com.auth.api.exceptions.RoleAlreadyAssignedException;
import com.auth.api.exceptions.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Role> getAllRoles() {
        return rolesRepository.getAllRoles();
    }

    public void assignRoleToUser(UUID userID, String roleType) {
        // Check if the role is already assigned to the user
        for (Role role : rolesRepository.getAllRoles()) {
            if (role.getUserId().equals(userID) && role.getRoleType().equalsIgnoreCase(roleType)) {
                throw new RoleAlreadyAssignedException("Role '" + roleType + "' is already assigned to user " + userService.getUserById(userID).getEmail().split("@")[0].toUpperCase());
            }
        }
        // If the role is not already assigned, add it to the user
        rolesRepository.assignRoleToUser(userID, roleType);
    }

    public void removeRoleFromUser(UUID userID, String roleType) throws RoleNotFoundException {
        boolean roleRemoved = rolesRepository.removeRoleFromUser(userID, roleType);
        if (!roleRemoved) {
            throw new RoleNotFoundException("Role type '" + roleType + "' was not found for user " + userService.getUserById(userID).getEmail().split("@")[0].toUpperCase());
        }
    }

    public List<UUID> getUsersByRoleType(String roleType) {
        return rolesRepository.getUsersByRoleType(roleType);
    }

}

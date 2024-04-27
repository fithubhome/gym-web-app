package com.auth.api.service;

import com.auth.api.repository.RolesRepository;
import com.auth.api.exceptions.RoleAlreadyAssignedException;
import com.auth.api.exceptions.RoleNotFoundException;
import com.auth.api.model.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private UserService userService;

    public List<String> getRoleByUserId(int userID) throws RoleNotFoundException {
        return rolesRepository.getRolesByUserID(userID);
    }

    public List<Roles> getAllRoles() {
        return rolesRepository.getAllRoles();
    }

    public void assignRoleToUser(int userID, String roleType) {
        // Check if the role is already assigned to the user
        for (Roles roles : rolesRepository.getAllRoles()) {
            if (roles.getUserId() == userID && roles.getRoleType().equalsIgnoreCase(roleType)) {
                throw new RoleAlreadyAssignedException("Role '" + roleType + "' is already assigned to user " + userService.getUserById(userID).getEmail().split("@")[0].toUpperCase());
            }
        }
        // If the role is not already assigned, add it to the user
        rolesRepository.assignRoleToUser(userID, roleType);
    }

    public void removeRoleFromUser(int userID, String roleType) throws RoleNotFoundException {
        boolean roleRemoved = rolesRepository.removeRoleFromUser(userID, roleType);
        if (!roleRemoved) {
            throw new RoleNotFoundException("Role type '" + roleType + "' was not found for user " + userService.getUserById(userID).getEmail().split("@")[0].toUpperCase());
        }
    }

    public List<Integer> getUsersByRoleType(String roleType) {
        return rolesRepository.getUsersByRoleType(roleType);
    }

}

package com.auth.api.repository;

import com.auth.api.model.Role;
import com.auth.api.model.User;
import com.auth.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RolesRepository {
    private final UserService userService;
    private final List<Role> roles = new ArrayList<>();

    @Autowired
    public RolesRepository(UserService userService) {
        this.userService = userService;
        initializeProfiles();
    }

    private void initializeProfiles() {
        List<User> users = userService.getAllUsers();
        // Initialize each user with the default "Member" role.
        users.forEach(user -> roles.add(new Role(user.getId(), "Member")));
    }

    public List<Role> getAllRoles() {
        return new ArrayList<>(roles);
    }

    public List<String> getRolesByUserID(UUID userID) {
        return roles.stream()
                .filter(role -> role.getUserId().equals(userID))
                .map(Role::getRoleType)
                .collect(Collectors.toList());
    }

    public void assignRoleToUser(UUID userID, String roleType) {
        boolean roleExists = roles.stream()
                .anyMatch(role -> role.getUserId().equals(userID) && role.getRoleType().equalsIgnoreCase(roleType));
        if (!roleExists) {
            roles.add(new Role(userID, roleType));
        }
    }

    public boolean removeRoleFromUser(UUID userID, String roleType) {
        return roles.removeIf(role -> role.getUserId().equals(userID) && role.getRoleType().equalsIgnoreCase(roleType));
    }
}


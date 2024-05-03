package com.auth.api.repository;

import com.auth.api.model.Role;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class RolesRepository {
    private final List<Role> roleData = new ArrayList<>() {{
        add(new Role(UUID.randomUUID(), "Member"));     // Tim
        add(new Role(UUID.randomUUID(), "Member"));     // Paul
        add(new Role(UUID.randomUUID(), "Member"));     // Cristina
        add(new Role(UUID.randomUUID(), "Member"));     // Simida
        add(new Role(UUID.randomUUID(), "Member"));     // Alin
        add(new Role(UUID.randomUUID(), "Member"));     // Flavi
        add(new Role(UUID.randomUUID(), "Admin"));      // Andrei
        add(new Role(UUID.randomUUID(), "Admin"));      // Mihai
        add(new Role(UUID.randomUUID(), "Trainer"));   // Alina
        add(new Role(UUID.randomUUID(), "Trainer"));   // Darius
        add(new Role(UUID.randomUUID(), "Trainer"));     // Andrei
        add(new Role(UUID.randomUUID(), "Trainer"));     // Mihai
        add(new Role(UUID.randomUUID(), "Staff"));      // Mike
    }};

    public List<Role> getAllRoles() {
        return roleData;
    }

    public List<UUID> getUsersByRoleType(String roleType) {
        return roleData.stream()
                .filter(role -> role.getRoleType().equalsIgnoreCase(roleType))
                .map(Role::getUserId)
                .collect(Collectors.toList());
    }

    public List<String> getRolesByUserID(UUID userID) {
        List<String> userRoles = new ArrayList<>();
        for (Role role : roleData) {
            if (role.getUserId() == userID) {
                userRoles.add(role.getRoleType());
            }
        }
        return userRoles;
    }

    public void assignRoleToUser(UUID userID, String roleType) {
        boolean userFound = false;
        for (Role role : roleData) {
            if (role.getUserId() == userID) {
                // User found
                // Check if the role type already exists for the user
                boolean roleExists = false;
                for (Role userRole : roleData) {
                    if (userRole.getUserId() == userID && userRole.getRoleType().equalsIgnoreCase(roleType)) {
                        roleExists = true;
                        break;
                    }
                }
                if (!roleExists) {
                    // Role type does not exist, add it
                    roleData.add(new Role(userID, roleType));
                }
                userFound = true;
                break;
            }
        }
        if (!userFound) {
            // If the user ID is not found, add a new entry with the role type
            roleData.add(new Role(userID, roleType));
        }
    }

    public boolean removeRoleFromUser(UUID userID, String roleType) {
        for (Iterator<Role> iterator = roleData.iterator(); iterator.hasNext(); ) {
            Role role = iterator.next();
            if (role.getUserId() == userID && role.getRoleType().equals(roleType)) {
                iterator.remove();
                return true; // Role successfully removed
            }
        }
        return false;
    }

}

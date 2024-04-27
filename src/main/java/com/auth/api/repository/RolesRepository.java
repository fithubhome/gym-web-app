package com.auth.api.repository;

import com.auth.api.model.Roles;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RolesRepository {
    private final List<Roles> rolesData = new ArrayList<>() {{
        add(new Roles(1, "Member"));     // Tim
        add(new Roles(3, "Member"));     // Paul
        add(new Roles(4, "Member"));     // Cristina
        add(new Roles(5, "Member"));     // Simida
        add(new Roles(6, "Member"));     // Alin
        add(new Roles(7, "Member"));     // Flavi
        add(new Roles(8, "Admin"));      // Andrei
        add(new Roles(9, "Admin"));      // Mihai
        add(new Roles(10, "Trainer"));   // Alina
        add(new Roles(11, "Trainer"));   // Darius
        add(new Roles(8, "Trainer"));     // Andrei
        add(new Roles(9, "Trainer"));     // Mihai
        add(new Roles(2, "Staff"));      // Mike
    }};

    public List<Roles> getAllRoles() {
        return rolesData;
    }

    public List<Integer> getUsersByRoleType(String roleType) {
        return rolesData.stream()
                .filter(roles -> roles.getRoleType().equalsIgnoreCase(roleType))
                .map(Roles::getUserId)
                .collect(Collectors.toList());
    }

    public List<String> getRolesByUserID(int userID) {
        List<String> userRoles = new ArrayList<>();
        for (Roles role : rolesData) {
            if (role.getUserId() == userID) {
                userRoles.add(role.getRoleType());
            }
        }
        return userRoles;
    }

    public void assignRoleToUser(int userID, String roleType) {
        boolean userFound = false;
        for (Roles roles : rolesData) {
            if (roles.getUserId() == userID) {
                // User found
                // Check if the role type already exists for the user
                boolean roleExists = false;
                for (Roles userRole : rolesData) {
                    if (userRole.getUserId() == userID && userRole.getRoleType().equalsIgnoreCase(roleType)) {
                        roleExists = true;
                        break;
                    }
                }
                if (!roleExists) {
                    // Role type does not exist, add it
                    rolesData.add(new Roles(userID, roleType));
                }
                userFound = true;
                break;
            }
        }
        if (!userFound) {
            // If the user ID is not found, add a new entry with the role type
            rolesData.add(new Roles(userID, roleType));
        }
    }

    public boolean removeRoleFromUser(int userID, String roleType) {
        for (Iterator<Roles> iterator = rolesData.iterator(); iterator.hasNext(); ) {
            Roles roles = iterator.next();
            if (roles.getUserId() == userID && roles.getRoleType().equals(roleType)) {
                iterator.remove();
                return true; // Role successfully removed
            }
        }
        return false;
    }

}

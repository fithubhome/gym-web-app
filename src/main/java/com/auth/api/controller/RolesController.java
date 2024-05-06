package com.auth.api.controller;

import com.auth.api.UserContext;
import com.auth.api.exceptions.RoleNotFoundException;
import com.auth.api.model.Role;
import com.auth.api.model.User;
import com.auth.api.service.RolesService;
import com.auth.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/role")
public class RolesController {
    @Autowired
    private RolesService rolesService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String getAllRoles(Model model, HttpSession session) {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        List<User> users = userService.getAllUsers();
        Map<UUID, String> userRolesMap = new HashMap<>();
        for (User user:users) {
            try {
                List<String> userRoles = rolesService.getRoleByUserId(user.getId());
                if (!userRoles.isEmpty()) {
                    String userRolesString = String.join(", ", userRoles);
                    userRolesMap.put(user.getId(), userRolesString);
                }
            } catch (RoleNotFoundException e) {
                userRolesMap.put(user.getId(), "No roles assigned");
            }
        }
        model.addAttribute("users", users);
        model.addAttribute("userRolesMap", userRolesMap);
        return "role/allRoles";
    }

    @GetMapping("/{userId}")
    public String modifyRoles(@PathVariable UUID userId, Model model, HttpSession session) throws RoleNotFoundException {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        User selectedUser = userService.getUserById(userId);
        List<String> userRoles = rolesService.getRoleByUserId(userId);
        if (selectedUser == null || userRoles == null) {
            return "redirect:/user/role";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("user", selectedUser);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("allRoles", rolesService.getAllRoles());
        return "role/modifyRoles";
    }

    @PostMapping("/{userId}/add-role")
    public String addRoleToUser(@PathVariable UUID userId, @RequestParam String roleType) throws RoleNotFoundException {
        // Check if the role already exists for the user
        List<String> userRoles = rolesService.getRoleByUserId(userId);
        if (userRoles.contains(roleType)) {
            // Role already exists for the user, redirect to the same page
            return "redirect:/role/" + userId;
        }
        // Role doesn't exist, proceed to assign it
        rolesService.assignRoleToUser(userId, roleType);
        return "redirect:/role/" + userId;
    }

    @PostMapping("/{userId}/remove-role")
    public String removeRoleFromUser(@PathVariable UUID userId, @RequestParam String roleType) throws RoleNotFoundException {
        rolesService.removeRoleFromUser(userId, roleType);
        return "redirect:/role/{userId}";
    }
}

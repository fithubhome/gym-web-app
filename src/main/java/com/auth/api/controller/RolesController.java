package com.auth.api.controller;

import com.auth.api.UserContext;
import com.auth.api.exceptions.RoleNotFoundException;
import com.auth.api.model.Roles;
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
        String sessionId = (String) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        List<Roles> allRoles = rolesService.getAllRoles();
        List<User> users = userService.getAllUsers();
        Map<Integer, String> userRolesMap = new HashMap<>();
        for (User user : users) {
            StringBuilder userRolesBuilder = new StringBuilder();
            for (Roles role : allRoles) {
                if (role.getUserId() == user.getId()) {
                    if (!userRolesBuilder.isEmpty()) {
                        userRolesBuilder.append(", ");
                    }
                    userRolesBuilder.append(role.getRoleType());
                }
            }
            if (!userRolesBuilder.isEmpty()) {
                String userEmail = user.getEmail().split("@")[0];
                userRolesMap.put(user.getId(), userEmail + ": " + userRolesBuilder);
            }
        }
        model.addAttribute("userRolesMap", userRolesMap);
        return "role/allRoles";
    }


    @GetMapping("/{userId}")
    public String modifyRoles(@PathVariable int userId, Model model, HttpSession session) throws RoleNotFoundException {
        String sessionId = (String) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        User selectedUser = userService.getUserById(userId);
        List<String> userRoles = rolesService.getRoleByUserId(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("user", selectedUser);
        model.addAttribute("userRoles", userRoles);
        List<String> allRoles = rolesService.getAllRoles()
                .stream()
                .map(Roles::getRoleType)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("allRoles", allRoles);
        return "role/modifyRoles";
    }

    @PostMapping("/{userId}/add-role")
    public String addRoleToUser(@PathVariable int userId, @RequestParam String roleType) throws RoleNotFoundException {
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
    public String removeRoleFromUser(@PathVariable int userId, @RequestParam String roleType) throws RoleNotFoundException {
        rolesService.removeRoleFromUser(userId, roleType);
        return "redirect:/role/{userId}";
    }
}

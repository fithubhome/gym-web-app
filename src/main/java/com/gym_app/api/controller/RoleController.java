package com.gym_app.api.controller;

import com.gym_app.api.exceptions.auth.RoleNotFoundException;
import com.gym_app.api.model.Role;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.security.CustomUserDetails;
import com.gym_app.api.service.RoleService;
import com.gym_app.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final List<String> AVAILABLE_ROLES = Arrays.asList("ADMIN", "TRAINER", "MEMBER", "STAFF");

    @GetMapping("")
    public String getAllRoles(Model model) {
        List<UserEntity> userEntities = userService.getAllUsers();
        Map<UUID, String> userRolesMap = new HashMap<>();
        for (UserEntity userEntity : userEntities) {
            List<Role> userRoles = userEntity.getRoles();
            String userRolesString = userRoles.stream().map(Role::getName).reduce((r1, r2) -> r1 + ", " + r2).orElse("");
            userRolesMap.put(userEntity.getId(), userRolesString);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userEntities", userEntities);
        model.addAttribute("userRolesMap", userRolesMap);
        return "role/index";
    }

    @GetMapping("/{userId}")
    public String modifyRoles(@PathVariable UUID userId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());

        UserEntity selectedUser = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + userId));

        List<Role> userRoles = selectedUser.getRoles();
        List<String> userRoleNames = userRoles.stream().map(Role::getName).toList();
        List<String> availableRoles = AVAILABLE_ROLES.stream()
                .filter(role -> !userRoleNames.contains(role))
                .collect(Collectors.toList());

        model.addAttribute("userId", userId);
        model.addAttribute("user", selectedUser);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("availableRoles", availableRoles);
        return "role/modify";
    }

    @PostMapping("/{userId}/add-role")
    public String addRoleToUser(@PathVariable UUID userId, @RequestParam String roleType) {
        roleService.assignRoleToUser(userId, roleType);
        return "redirect:/role/" + userId;
    }

    @PostMapping("/{userId}/remove-role")
    public String removeRoleFromUser(@PathVariable UUID userId, @RequestParam String roleType) throws RoleNotFoundException {
        roleService.removeRoleFromUser(userId, roleType);
        return "redirect:/role/" + userId;
    }
}

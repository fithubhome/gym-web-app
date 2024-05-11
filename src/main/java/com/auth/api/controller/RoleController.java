package com.auth.api.controller;

import com.auth.api.UserContext;
import com.auth.api.exceptions.RoleNotFoundException;
import com.auth.api.model.User;
import com.auth.api.service.RoleService;
import com.auth.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
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
        for (User user : users) {
            try {
                List<String> userRoles = roleService.getRoleByUserId(user.getId());
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
        return "role/role";
    }

    @GetMapping("/{userId}")
    public String modifyRoles(@PathVariable UUID userId, Model model, HttpSession session) throws RoleNotFoundException {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return "redirect:/user/login";
        }
        User selectedUser = userService.findUserById(userId);
        List<String> userRoles = roleService.getRoleByUserId(userId);
        if (selectedUser == null || userRoles == null) {
            return "redirect:/role";
        }
        model.addAttribute("userId", userId);
        model.addAttribute("user", selectedUser);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "role/modify";
    }

    @PostMapping("/{userId}/add-role")
    public String addRoleToUser(@PathVariable UUID userId, @RequestParam String roleType) throws RoleNotFoundException {
        List<String> userRoles = roleService.getRoleByUserId(userId);
        if (userRoles.contains(roleType)) {
            return "redirect:/role/" + userId;
        }
        roleService.assignRoleToUser(userId, roleType);
        return "redirect:/role/" + userId;
    }

    @PostMapping("/{userId}/remove-role")
    public String removeRoleFromUser(@PathVariable UUID userId, @RequestParam String roleType) throws RoleNotFoundException {
        roleService.removeRoleFromUser(userId, roleType);
        return "redirect:/role/" + userId;
    }
}

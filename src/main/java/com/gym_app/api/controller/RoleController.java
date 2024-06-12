package com.gym_app.api.controller;

import com.gym_app.api.exceptions.RoleNotFoundException;
import com.gym_app.api.model.Role;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.service.RoleService;
import com.gym_app.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public String getAllRoles(Model model) {
        List<UserEntity> userEntities = userService.getAllUsers();
        Map<UUID, String> userRolesMap = new HashMap<>();
        for (UserEntity userEntity : userEntities) {
            List<Role> userRoles = userEntity.getRoles();
            String userRolesString = userRoles.stream().map(Role::getName).reduce((r1, r2) -> r1 + ", " + r2).orElse("");
            userRolesMap.put(userEntity.getId(), userRolesString);
        }
        model.addAttribute("userEntities", userEntities);
        model.addAttribute("userRolesMap", userRolesMap);
        return "role/index";
    }

    @GetMapping("/{userId}")
    public String modifyRoles(@PathVariable UUID userId, Model model) {
        UserEntity selectedUserEntity = userService.findUserById(userId);
        if (selectedUserEntity == null) {
            return "redirect:/role";
        }
        List<Role> userRoles = selectedUserEntity.getRoles();
        model.addAttribute("userId", userId);
        model.addAttribute("user", selectedUserEntity);
        model.addAttribute("userRoles", userRoles);
        return "role/modify";
    }

    @PostMapping("/{userId}/add-role")
    public String addRoleToUser(@PathVariable UUID userId, @RequestParam String roleType) throws RoleNotFoundException {
        roleService.assignRoleToUser(userId, roleType);
        return "redirect:/role/" + userId;
    }

    @PostMapping("/{userId}/remove-role")
    public String removeRoleFromUser(@PathVariable UUID userId, @RequestParam String roleType) throws RoleNotFoundException {
        roleService.removeRoleFromUser(userId, roleType);
        return "redirect:/role/" + userId;
    }
}

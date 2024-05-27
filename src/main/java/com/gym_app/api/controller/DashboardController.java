package com.gym_app.api.controller;

import com.gym_app.api.UserContext;
import com.gym_app.api.exceptions.RoleNotFoundException;
import com.gym_app.api.model.Profile;
import com.gym_app.api.model.Role;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.RoleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserEntity userEntity;

    @GetMapping("")
    public String getDashboard(Model model, HttpSession session) throws RoleNotFoundException {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        UserEntity currentUserEntity = UserContext.getCurrentUser(sessionId);
        if (currentUserEntity == null) {
            return "redirect:/";
        }
        Profile currentUserProfile = profileService.findProfileByUserId(currentUserEntity.getId());
        List<Role> currentUserRole = userEntity.getRoles();
        model.addAttribute("profile", currentUserProfile);
        if (currentUserProfile.getImageData() != null) {
            String base64Image = Base64.getEncoder().encodeToString(currentUserProfile.getImageData());
            model.addAttribute("base64Image", base64Image);
        }
        model.addAttribute("role", currentUserRole);
        model.addAttribute("user", currentUserEntity);
        return "core/dashboard";
    }
}

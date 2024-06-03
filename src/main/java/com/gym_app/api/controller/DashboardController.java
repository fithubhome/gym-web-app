package com.gym_app.api.controller;

import com.gym_app.api.model.Profile;
import com.gym_app.api.model.Role;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.security.CustomUserDetails;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity userEntity = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(userEntity.getId());
        List<Role> roles = userEntity.getRoles();

        if (profile != null && profile.getImageData() != null) {
            String base64Image = Base64.getEncoder().encodeToString(profile.getImageData());
            model.addAttribute("base64Image", base64Image);
        } else {
            model.addAttribute("base64Image", null);
        }

        model.addAttribute("profile", profile);
        model.addAttribute("roles", roles);
        model.addAttribute("user", userEntity);

        return "core/dashboard";
    }
}

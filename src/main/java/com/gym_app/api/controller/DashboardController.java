package com.bodystats.api.controller;

import com.bodystats.api.model.Profile;
import com.bodystats.api.model.Role;
import com.bodystats.api.model.UserEntity;
import com.bodystats.api.security.CustomUserDetails;
import com.bodystats.api.service.ProfileService;
import com.bodystats.api.service.UserService;
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

        boolean isAdmin = roles.stream().map(Role::getName).toList().contains("ADMIN");

        model.addAttribute("profile", profile);
        model.addAttribute("roles", roles);
        model.addAttribute("user", userEntity);
        model.addAttribute("isAdmin", isAdmin);

        return "core/dashboard";
    }
}

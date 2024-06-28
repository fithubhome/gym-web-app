package com.gym_app.api.controller.external;

import com.gym_app.api.dto.bodystats.BodyStatsDTO;
import com.gym_app.api.model.Profile;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.security.CustomUserDetails;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.UserService;
import com.gym_app.api.service.external.bodystats.BodystatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/bodystats")
public class BodystatsController {
    @Autowired
    BodystatsService bodystatsService;
    @Autowired
    UserService userService;
    @Autowired
    ProfileService profileService;
    @GetMapping
    public String getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        return "bodystats/index";
    }

    @PostMapping
    public String createNewBodyStats(@ModelAttribute BodyStatsDTO bodyStats) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(currentUser.getId());
        bodyStats.setProfileId(profile.getId());
        bodystatsService.createBodyStatsRecords(bodyStats);
        return "redirect:/bodystats";
    }

    @GetMapping("/history")
    public String getBodyStatsHistory(Model model, UUID profileId) {
        model.addAttribute("body", bodystatsService.getBodystatsByProfileId(profileId));
        return "/bodystats/bodystats-history";
    }
/*    @GetMapping("/record")
    public String createBodyStatsRecords(Model model) {
        return "/bodystats/bodystats-records";
    }*/
    @GetMapping("/record")
    public String showNewBodyStatsForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(currentUser.getId());
        BodyStatsDTO bodyStats = new BodyStatsDTO();
        bodyStats.setProfileId(profile.getId());
        model.addAttribute("body", bodyStats);
        model.addAttribute("profileId", profile.getId());
        return "/bodystats/bodystats-records";
    }


}

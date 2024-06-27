package com.gym_app.api.controller.external;

import com.gym_app.api.model.Profile;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.security.CustomUserDetails;
import com.gym_app.api.service.ProfileService;
import com.gym_app.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.gym_app.api.dto.bodystats.BodystatsDTO;
import com.gym_app.api.service.external.bodystats.BodystatsService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/bodystats")
public class BodystatsController {
    @Autowired
    private BodystatsService bodyStatsService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(BodystatsController.class);

    @GetMapping
    public String getLastBodyStats(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(currentUser.getId());
        BodystatsDTO bodyStats = bodyStatsService.getLastBodystats(profile.getId());
        model.addAttribute("bodyStats", bodyStats);
        model.addAttribute("profileId", profile.getId());
        return "/bodystats/index";
    }

    @GetMapping("/history")
    public String getBodyStatsHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(currentUser.getId());
        List<BodystatsDTO> bodyStatsList = bodyStatsService.getBodystatsByProfileId(profile.getId());
        model.addAttribute("bodyStatsList", bodyStatsList);
        model.addAttribute("profileId", profile.getId());
        return "/bodystats/history";
    }

    @GetMapping("/{id}")
    public String getBodyStatsById(@PathVariable UUID id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(currentUser.getId());
        Optional<BodystatsDTO> bodyStats = bodyStatsService.getBodystatsById(id);
        model.addAttribute("bodyStats", bodyStats.orElse(null));
        model.addAttribute("profileId", profile.getId());
        return "/bodystats/details";
    }

    @GetMapping("/new")
    public String showNewBodyStatsForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(currentUser.getId());
        BodystatsDTO bodyStats = new BodystatsDTO();
        bodyStats.setProfileId(profile.getId());
        model.addAttribute("bodyStats", bodyStats);
        model.addAttribute("profileId", profile.getId());
        return "/bodystats/new";
    }

    @PostMapping("/new")
    public String createNewBodyStats(@ModelAttribute BodystatsDTO bodyStats) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity currentUser = userService.findByEmail(userDetails.getUsername());
        Profile profile = profileService.findProfileByUserId(currentUser.getId());
        bodyStats.setProfileId(profile.getId());
        bodyStatsService.saveBodystats(bodyStats);
        return "redirect:/bodystats";
    }
}

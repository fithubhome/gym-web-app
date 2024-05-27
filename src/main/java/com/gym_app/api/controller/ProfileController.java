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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserEntity userEntity;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/{profileId}")
    public Profile getProfileById(HttpSession session) {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        UserEntity currentUserEntity = UserContext.getCurrentUser(sessionId);
        if (currentUserEntity == null) {
            return new Profile();
        }
        Profile currentProfile = profileService.findProfileByUserId(currentUserEntity.getId());
        return ResponseEntity.ok(currentProfile).getBody();
    }

    @GetMapping("")
    public ModelAndView getProfile(HttpSession session) throws RoleNotFoundException {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        UserEntity currentUserEntity = UserContext.getCurrentUser(sessionId);
        if (currentUserEntity == null) {
            return new ModelAndView("redirect:/user/login");
        }
        Profile profile = profileService.findProfileByUserId(currentUserEntity.getId());
        List<Role> currentRole = userEntity.getRoles();
;        if (profile == null) {
            profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setUserId(currentUserEntity.getId());
            profile.setDob(new Date());
            profileService.createProfile(profile);
        }
        ModelAndView mav = new ModelAndView("profile/profile");
        mav.addObject("profile", profile);
        mav.addObject("role", currentRole);
        if (profile.getImageData() != null) {
            String base64Image = Base64.getEncoder().encodeToString(profile.getImageData());
            mav.addObject("base64Image", base64Image);
        }
        return mav;
    }

    @GetMapping("/edit")
    public ModelAndView editProfile(HttpSession session) {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        UserEntity currentUserEntity = UserContext.getCurrentUser(sessionId);
        if (currentUserEntity == null) {
            return new ModelAndView("redirect:/user/login");
        }
        Profile profile = profileService.findProfileByUserId(currentUserEntity.getId());
        if (profile == null) {
            profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setUserId(currentUserEntity.getId());
            profileService.createProfile(profile);
        }
        ModelAndView mav = new ModelAndView("profile/update");
        mav.addObject("profile", profile);
        return mav;
    }

    @PostMapping("/update")
    public ModelAndView updateProfile(HttpSession session,
                                      @ModelAttribute Profile updatedProfile,
                                      @RequestParam("fileInput") MultipartFile fileInput) throws IOException {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        UserEntity currentUserEntity = UserContext.getCurrentUser(sessionId);
        if (currentUserEntity == null) {
            return new ModelAndView("redirect:/user/login");
        }
        updatedProfile.setUserId(currentUserEntity.getId());
        if (!fileInput.isEmpty()) {
            updatedProfile.setImageData(fileInput.getBytes());
        }
        profileService.updateProfile(updatedProfile);
        return new ModelAndView("redirect:/profile");
    }
}

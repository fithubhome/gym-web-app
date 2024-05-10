package com.auth.api.controller;

import com.auth.api.UserContext;
import com.auth.api.model.Profile;
import com.auth.api.model.User;
import com.auth.api.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("")
    public ModelAndView getProfile(HttpSession session) {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return new ModelAndView("redirect:/user/login");
        }
        Profile profile = profileService.getProfileByUserId(currentUser.getId());
        if (profile == null) {
            profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setUserId(currentUser.getId());
            profile.setDob(new Date()); // Initialize dob
            profileService.createProfile(profile);
        }
        ModelAndView mav = new ModelAndView("profile/main");
        mav.addObject("profile", profile);
        if (profile.getImageData() != null) {
            String base64Image = Base64.getEncoder().encodeToString(profile.getImageData());
            mav.addObject("base64Image", base64Image);
        }
        return mav;
    }

    @GetMapping("/edit")
    public ModelAndView editProfile(HttpSession session) {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return new ModelAndView("redirect:/user/login");
        }
        Profile profile = profileService.getProfileByUserId(currentUser.getId());
        if (profile == null) {
            profile = new Profile();
            profile.setId(UUID.randomUUID());
            profile.setUserId(currentUser.getId());
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
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return new ModelAndView("redirect:/user/login");
        }
        updatedProfile.setUserId(currentUser.getId());
        if (!fileInput.isEmpty()) {
            updatedProfile.setImageData(fileInput.getBytes());
        }
        profileService.updateProfile(updatedProfile);
        return new ModelAndView("redirect:/profile");
    }
}

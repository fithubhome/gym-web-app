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
import org.springframework.web.servlet.ModelAndView;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String sessionId = (String) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return new ModelAndView("redirect:/user/login");
        }

        Profile profile = profileService.getProfileByUserId(currentUser.getId());
        ModelAndView mav = new ModelAndView("profile/main");
        mav.addObject("profile", profile);
        return mav;
    }

    @GetMapping("/edit")
    public ModelAndView editProfile(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return new ModelAndView("redirect:/user/login");
        }
        Profile profile = profileService.getProfileByUserId(currentUser.getId());
        if (profile == null) {
            profile = new Profile(); // or handle this case specifically
        }

        ModelAndView mav = new ModelAndView("profile/update");
        mav.addObject("profile", profile);
        return mav;
    }
    @PostMapping("/update")
    public ModelAndView updateProfile(HttpSession session, @ModelAttribute Profile updatedProfile) {
        String sessionId = (String) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return new ModelAndView("redirect:/user/login");
        }
        updatedProfile.setUserId(currentUser.getId());
        profileService.updateProfile(updatedProfile);
        return new ModelAndView("redirect:/profile");
    }

}

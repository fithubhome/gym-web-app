package com.auth.api.controller;

import com.auth.api.UserContext;
import com.auth.api.exceptions.DuplicateUserException;
import com.auth.api.model.Profile;
import com.auth.api.model.User;
import com.auth.api.service.ProfileService;
import com.auth.api.service.RoleService;
import com.auth.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private User user;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        User user = userService.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            UUID sessionId = UUID.randomUUID();
            session.setAttribute("sessionId", sessionId);
            UserContext.loginUser(sessionId, user);
            return "redirect:/user/dashboard";
        }

        model.addAttribute("errorMessage", "Invalid email or password");
        return "auth/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User newUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            newUser.setId(UUID.randomUUID());
            User addedUser = userService.addUser(newUser);
            Profile newProfile = new Profile(UUID.randomUUID(), addedUser.getId());
            profileService.createProfile(newProfile);
            // Assign the default role of 'member' to the new user
            roleService.assignRoleToUser(addedUser.getId(), "Member");
            return "redirect:/user/login";
        } catch (DuplicateUserException e) {
            model.addAttribute("errorMessage", "User with email " + newUser.getEmail() + " already exists.");
            return "error";
        }
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model, HttpSession session) {
        UUID sessionId = (UUID) session.getAttribute("sessionId");
        User currentUser = UserContext.getCurrentUser(sessionId);
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        return "core/dashboard";
    }

    @GetMapping("/logout")
    public String logoutUser(@RequestParam("sessionId") UUID sessionId, HttpSession session) {
        UserContext.logoutUser(sessionId);
        session.invalidate();
        return "redirect:/";
    }
}

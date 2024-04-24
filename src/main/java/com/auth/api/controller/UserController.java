package com.auth.api.controller;

import com.auth.api.ApplicationContext;
import com.auth.api.exceptions.DuplicateUserException;
import com.auth.api.model.User;
import com.auth.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        User user = userService.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            ApplicationContext.setCurrentUser(user);
            return "redirect:/user/dashboard";
        }

        model.addAttribute("errorMessage", "Invalid email or password");
        return "auth/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User newUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            User addedUser = userService.addUser(newUser);
            return "redirect:/user/login";
        } catch (DuplicateUserException e) {
            model.addAttribute("errorMessage", "User with email " + newUser.getEmail() + " already exists.");
            return "error";
        }
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        User currentUser = ApplicationContext.getCurrentUser();
        model.addAttribute("user", currentUser);
        if (currentUser != null) {
            return "core/dashboard";
        } else {
            return "redirect:/user/login";
        }
    }

    public boolean authenticateUser(String email, String password) {
        User user = userService.findByEmail(email);

        return user != null && user.getPassword().equals(password) && user.getEmail().equals(email);
    }

}

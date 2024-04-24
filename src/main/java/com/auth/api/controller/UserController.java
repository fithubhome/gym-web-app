package com.auth.api.controller;

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
        // Implement your authentication logic here
        if (authenticateUser(email, password)) {
            // Redirect to a success page or dashboard
            return "redirect:/dashboard";
        } else {
            // If authentication fails, return to login page with error message
            model.addAttribute("errorMessage", "Invalid email or password");
            return "auth/login";
        }
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
            // Optionally, you can redirect to the login page after successful registration
            return "redirect:/users/login";
        } catch (DuplicateUserException e) {
            model.addAttribute("errorMessage", "User with email " + newUser.getEmail() + " already exists.");
            return "error";
        }
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        // Retrieve user-specific data to display on the dashboard
        // You can fetch user details from the session or database and pass them to the view
        return "core/dashboard";
    }

    public boolean authenticateUser(String email, String password) {
        // Retrieve user by email from your data source
        User user = userService.findByEmail(email);

        // Check if the user exists and if the provided password matches
        if (user != null && user.getPassword().equals(password)) {
            return true; // Authentication successful
        }

        return false; // Authentication failed
    }

}

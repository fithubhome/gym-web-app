package com.gym_app.api.controller;

import com.gym_app.api.dto.LoginDTO;
import com.gym_app.api.dto.RegisterDTO;
import com.gym_app.api.exceptions.DuplicateUserException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.repository.UserRepository;
import com.gym_app.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new LoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDto") LoginDTO loginDto, Model model) {
        try {
            UserEntity user = userService.findByEmail(loginDto.getEmail());
            if (user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                model.addAttribute("error", "Invalid username or password");
                return "auth/login";
            }
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during login");
            return "auth/login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerUser") RegisterDTO registerUser, Model model) throws DuplicateUserException {
        if (userRepository.existsByEmail(registerUser.getEmail())) {
            model.addAttribute("error", "Email is already taken!");
            return "auth/register";
        }
        UserEntity user = new UserEntity();
        user.setEmail(registerUser.getEmail());
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        userService.registerNewUser(user);
        return "redirect:/auth/login";
    }
}

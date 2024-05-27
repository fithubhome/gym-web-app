package com.gym_app.api.controller;

import com.gym_app.api.UserContext;
import com.gym_app.api.exceptions.DuplicateUserException;
import com.gym_app.api.exceptions.RoleNotFoundException;
import com.gym_app.api.model.UserEntity;
import com.gym_app.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/userEntity")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserEntity userEntity;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, Model model) {
        UserEntity userEntity = userService.findByEmail(email);
        if (userEntity != null && userEntity.getPassword().equals(password)) {
            UUID sessionId = UUID.randomUUID();
            session.setAttribute("sessionId", sessionId);
            UserContext.loginUser(sessionId, userEntity);
            return "redirect:/dashboard";
        }

        model.addAttribute("errorMessage", "Invalid email or password");
        return "auth/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("userEntity", userEntity);
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userEntity") @Valid UserEntity newUserEntity, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            newUserEntity.setId(UUID.randomUUID());
            UserEntity addedUserEntity = userService.addUser(newUserEntity);
            return "redirect:/userEntity/login";
        } catch (DuplicateUserException e) {
            model.addAttribute("errorMessage", "UserEntity with email " + newUserEntity.getEmail() + " already exists.");
            return "error";
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public String logoutUser(@RequestParam("sessionId") UUID sessionId, HttpSession session) {
        UserContext.logoutUser(sessionId);
        session.invalidate();
        return "redirect:/";
    }
}

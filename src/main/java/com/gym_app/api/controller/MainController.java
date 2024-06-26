package com.gym_app.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping("")
    public String mainPage(Model model) {
        model.addAttribute("message", "Welcome to FitHubHome!");
        return "index";
    }
}

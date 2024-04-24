package com.auth.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("")
    public String mainPage(Model model) {
        // Add any data you want to pass to the view
        model.addAttribute("message", "Welcome to the application!");

        // Return the view name
        return "index";
    }
}

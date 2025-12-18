package com.forum.forum.web.controller;

import com.forum.forum.service.UserService;
import com.forum.forum.to.RegistrationUserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {

    @Autowired
    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationTo", new RegistrationUserTo());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegistrationUserTo registrationTo, Model model) {
        try {
            userService.register(registrationTo);
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}

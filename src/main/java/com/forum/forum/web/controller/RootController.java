package com.forum.forum.web.controller;

import com.forum.forum.service.UserService;
import com.forum.forum.to.RegistrationUserTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    private final UserService userService;

    public RootController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        log.info("Open home page");
        return "home";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        log.debug("Open register page");
        model.addAttribute("registrationTo", new RegistrationUserTo());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegistrationUserTo registrationTo,
                           Model model) {

        log.info("Registration attempt: email={}, login={}",
                registrationTo.getEmail(),
                registrationTo.getLogin());

        try {
            userService.register(registrationTo);
            log.info("User successfully registered: {}",
                    registrationTo.getEmail());
            return "redirect:/login";

        } catch (IllegalArgumentException ex) {
            log.warn("Registration failed for {}: {}",
                    registrationTo.getEmail(),
                    ex.getMessage());

            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}

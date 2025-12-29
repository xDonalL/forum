package com.forum.forum.web.controller;

import com.forum.forum.service.UserService;
import com.forum.forum.to.LoginTo;
import com.forum.forum.to.RegistrationUserTo;
import com.forum.forum.util.exception.EmailAlreadyExistsException;
import com.forum.forum.util.exception.LoginAlreadyExistsException;
import com.forum.forum.util.exception.PasswordMismatchException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String register(@Valid @ModelAttribute("registrationTo") RegistrationUserTo registrationTo,
                           BindingResult bindingResult) {

        log.info("Registration attempt: email={}, login={}",
                registrationTo.getEmail(), registrationTo.getLogin());

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.register(registrationTo);
            log.info("User successfully registered: {}", registrationTo.getEmail());
            return "redirect:/login";

        } catch (EmailAlreadyExistsException ex) {
            bindingResult.rejectValue(
                    "email", "email.exists", "email is already in use");
        } catch (LoginAlreadyExistsException ex) {
            bindingResult.rejectValue(
                    "login", "login.exists", "login is already in use");
        } catch (PasswordMismatchException ex) {
            bindingResult.rejectValue(
                    "confirmPassword", "password.mismatch", "password mismatch");
        }
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            Model model) {
        log.info("Open login page");

        if (error != null) {
            model.addAttribute("loginError", "Incorrect email or password");
        }

        model.addAttribute("loginTo", new LoginTo());
        return "login";
    }
}


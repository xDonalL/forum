package com.forum.forum.web.controller;

import com.forum.forum.model.User;
import com.forum.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profilePage(Model model, Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        model.addAttribute("user", user);
        return "user-profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute("user") User userForm, Authentication auth) {
        User user = userService.getByEmail(auth.getName());

        user.setName(userForm.getName());

        userService.update(user);

        return "redirect:/profile";
    }
}

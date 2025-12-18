package com.forum.forum.web.controller;

import com.forum.forum.model.User;
import com.forum.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}-{login}")
    public String profilePage(Model model, @PathVariable Integer id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, Authentication auth) {
        User user = userService.getByEmail(auth.getName());
        model.addAttribute("user", user);
        return "profile/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute("user") User userForm,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                Authentication auth) throws IOException {
        User user = userService.getByEmail(auth.getName());

        user.setName(userForm.getName());

        userService.update(user, avatarFile);

        return "redirect:/profile/" + user.getId() + "-" + user.getLogin();
    }
}

package com.forum.forum.web.controller;

import com.forum.forum.model.User;
import com.forum.forum.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}-{login}")
    public String profilePage(Model model,
                              @PathVariable Integer id) {

        log.info("Open profile page: userId={}", id);

        User user = userService.getUserById(id);
        model.addAttribute("user", user);

        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfile(Model model, Authentication auth) {

        String email = auth.getName();
        log.info("Open profile edit page: email={}", email);

        User user = userService.getByEmail(email);
        model.addAttribute("user", user);

        return "profile/edit";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute("user") User userForm,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                Authentication auth) throws IOException {

        String email = auth.getName();
        User user = userService.getByEmail(email);

        log.info("Update profile: userId={}, avatarUploaded={}",
                user.getId(),
                avatarFile != null && !avatarFile.isEmpty());

        user.setName(userForm.getName());

        userService.update(user, avatarFile);

        return "redirect:/profile/" + user.getId() + "-" + user.getLogin();
    }
}

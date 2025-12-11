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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}-{login}")
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
    public String updateProfile(@ModelAttribute("user") User userForm,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                Authentication auth) throws IOException {
        User user = userService.getByEmail(auth.getName());

        user.setName(userForm.getName());

        if (!avatarFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
            Path uploadPath = Paths.get("/uploads/avatars");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            avatarFile.transferTo(uploadPath.resolve(filename));
            user.setAvatar(filename);
        }

        userService.update(user);

        return "redirect:/profile";
    }
}

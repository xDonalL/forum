package com.forum.forum.web.controller;

import com.forum.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @PostMapping("/{id}/ban")
    public String banUser(@PathVariable int id) {
        userService.banUser(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/unban")
    public String unbanUser(@PathVariable int id) {
        userService.unbanUser(id);
        return "redirect:/admin/users";
    }
}

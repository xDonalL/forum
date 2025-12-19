package com.forum.forum.web.controller;

import com.forum.forum.model.User;
import com.forum.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/panel")
    public String usersPage(@RequestParam(required = false) String filter,
                            Model model) {
        List<User> users = userService.filterUsers(filter);
        model.addAttribute("users", users);
        return "admin/panel";
    }

    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable int id) {
        userService.banUser(id);
        return "redirect:/admin/panel";
    }

    @PostMapping("/unban/{id}")
    public String unbanUser(@PathVariable int id) {
        userService.unbanUser(id);
        return "redirect:/admin/panel";
    }
}

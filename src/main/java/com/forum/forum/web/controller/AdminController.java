package com.forum.forum.web.controller;

import com.forum.forum.model.User;
import com.forum.forum.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/panel")
    public String showUsersPanel(@RequestParam(required = false) String filter,
                                 @RequestParam(required = false) String type,
                                 @RequestParam(required = false) String q,
                                 Model model) {

        log.debug("Admin panel request: filter={}, type={}, q={}",
                filter, type, q);

        List<User> users;
        if (q != null && !q.isBlank()) {
            users = userService.search(q, type);
            log.info("Admin searched users: query='{}', type='{}'", q, type);
        } else {
            users = userService.filterUsers(filter);
            log.info("Admin filtered users: filter='{}'", filter);
        }

        model.addAttribute("users", users);
        return "admin/panel";
    }

    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable int id) {
        log.info("Admin banning user: id={}", id);
        userService.banUser(id);
        return "redirect:/admin/panel";
    }

    @PostMapping("/unban/{id}")
    public String unbanUser(@PathVariable int id) {
        log.info("Admin unbanning user: id={}", id);
        userService.unbanUser(id);
        return "redirect:/admin/panel";
    }
}

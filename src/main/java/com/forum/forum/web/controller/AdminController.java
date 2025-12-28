package com.forum.forum.web.controller;

import com.forum.forum.model.AdminLog;
import com.forum.forum.model.User;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.forum.forum.model.ActionLog.BAN_USER;
import static com.forum.forum.model.ActionLog.UNBAN_USER;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final AdminLogService adminLogService;
    private final AdminLogService logService;

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
    public String banUser(@PathVariable int id,
                          Authentication auth) {
        log.info("Admin banning user: id={}", id);
        userService.banUser(id);
        User user = userService.getUserById(id);
        AuthorizedUser authorizedUser = (AuthorizedUser) auth.getPrincipal();
        logService.logAction(authorizedUser.getUser().getLogin(),
                BAN_USER, user.getLogin(), String.valueOf(id));
        return "redirect:/admin/panel";
    }

    @PostMapping("/unban/{id}")
    public String unbanUser(@PathVariable int id,
                            Authentication auth) {
        log.info("Admin unbanning user: id={}", id);
        userService.unbanUser(id);
        User user = userService.getUserById(id);

        AuthorizedUser authorizedUser = (AuthorizedUser) auth.getPrincipal();
        logService.logAction(authorizedUser.getUser().getLogin(),
                UNBAN_USER, user.getLogin(), String.valueOf(id));
        return "redirect:/admin/panel";
    }

    @GetMapping("/log")
    public String showLogs(@RequestParam(required = false) String username,
                           Model model) {
        log.debug("Admin logs request");

        List<AdminLog> logs;

        if (username != null && !username.isBlank()) {
            log.info("Search log by query: '{}'", username);
            logs = adminLogService.searchByUsername(username);
        } else {
            logs = adminLogService.getAll();
        }

        model.addAttribute("logs", logs);
        return "admin/log";
    }
}

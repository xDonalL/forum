package com.forum.forum.web.controller;

import com.forum.forum.model.AdminLog;
import com.forum.forum.model.User;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.AdminService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.forum.forum.model.ActionLog.BAN_USER;
import static com.forum.forum.model.ActionLog.UNBAN_USER;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final AdminLogService logService;
    private final UserService userService;

    @GetMapping("/panel")
    public String showUsersPanel(@RequestParam(required = false) String filter,
                                 @RequestParam(required = false) String type,
                                 @RequestParam(required = false) String q,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {

        log.debug("Admin panel request: filter={}, type={}, q={}",
                filter, type, q);

        Page<User> pageUsers;
        if (q != null && !q.isBlank()) {
            pageUsers = adminService.search(page, 10, q, type);
            log.info("Admin searched users: query='{}', type='{}'", q, type);
        } else {
            pageUsers = adminService.filterUsers(page, 10, filter);
            log.info("Admin filtered users: filter='{}'", filter);
        }

        model.addAttribute("pageUsers", pageUsers);
        model.addAttribute("baseUrl", "/admin/panel");
        model.addAttribute("filter", filter);
        model.addAttribute("type", type);
        model.addAttribute("q", q);

        return "admin/panel";
    }

    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable int id,
                          Authentication auth) {

        log.info("Admin banning user: id={}", id);

        String authorLogin = userService.getUserById(id).getLogin();

        adminService.banUser(id);

        AuthorizedUser authorizedUser = (AuthorizedUser) auth.getPrincipal();
        logService.logAction(authorizedUser.getUser().getLogin(),
                BAN_USER, authorLogin, id);
        return "redirect:/admin/panel";
    }

    @PostMapping("/unban/{id}")
    public String unbanUser(@PathVariable int id,
                            Authentication auth) {
        log.info("Admin unbanning user: id={}", id);

        String authorLogin = userService.getUserById(id).getLogin();

        adminService.unbanUser(id);

        AuthorizedUser authorizedUser = (AuthorizedUser) auth.getPrincipal();
        logService.logAction(authorizedUser.getUser().getLogin(),
                UNBAN_USER, authorLogin, id);
        return "redirect:/admin/panel";
    }

    @GetMapping("/log")
    public String showLogs(@RequestParam(required = false) String q,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        log.debug("Admin logs request");

        Page<AdminLog> pageLogs;

        if (q != null && !q.isBlank()) {
            log.info("Search log by query: '{}'", q);
            pageLogs = logService.searchByUsername(page, 10, q);
        } else {
            pageLogs = logService.getAll(page, 10);
        }

        model.addAttribute("pageLogs", pageLogs);
        model.addAttribute("q", q);
        model.addAttribute("baseUrl", "/admin/log");

        return "admin/log";
    }
}

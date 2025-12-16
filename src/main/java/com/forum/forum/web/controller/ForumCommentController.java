package com.forum.forum.web.controller;

import com.forum.forum.model.User;
import com.forum.forum.service.ForumCommentService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/topic/comment")
public class ForumCommentController {

    private final ForumCommentService messageService;
    private final UserService userService;

    @PostMapping("/add")
    public String addComment(@RequestParam Integer topicId,
                             @RequestParam String comment) {

        User user = userService.getCurrentUser();
        messageService.addComment(topicId, user, comment);

        return "redirect:/forum/" + topicId;
    }
}

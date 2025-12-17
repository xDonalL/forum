package com.forum.forum.web.controller;

import com.forum.forum.model.User;
import com.forum.forum.service.ForumCommentService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/topic/comment")
public class ForumCommentController {

    private final ForumCommentService commentService;
    private final UserService userService;

    @PostMapping("/add")
    public String addComment(@RequestParam Integer topicId,
                             @RequestParam String comment) {

        User user = userService.getCurrentUser();
        commentService.addComment(topicId, user, comment);

        return "redirect:/forum/" + topicId;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Integer commentId,
                                @RequestParam Integer topicId) {
        commentService.delete(commentId);
        return "redirect:/forum/" + topicId;
    }
}

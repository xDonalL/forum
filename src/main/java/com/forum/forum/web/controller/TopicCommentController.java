package com.forum.forum.web.controller;

import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;
import com.forum.forum.service.TopicCommentService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/topic/comment")
public class TopicCommentController {

    private final TopicCommentService commentService;
    private final UserService userService;

    @PostMapping("/add")
    public String addComment(@RequestParam Integer topicId,
                             @RequestParam String comment) {

        User user = userService.getCurrentUser();
        commentService.addComment(topicId, user, comment);

        return "redirect:/forum/" + topicId;
    }

    @PreAuthorize("@commentSecurity.isOwner(#id)")
    @GetMapping("/edit/{id}")
    public String editCommentPage(@PathVariable Integer id, Model model) {
        TopicComment comment = commentService.get(id);
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PreAuthorize("@commentSecurity.isOwner(#id)")
    @PostMapping("/edit/{id}")
    public String editComment(@PathVariable("id") Integer id,
                              @RequestParam String text,
                              @RequestParam Integer topicId) {
        commentService.update(id, text);
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

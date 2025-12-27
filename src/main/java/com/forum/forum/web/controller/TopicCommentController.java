package com.forum.forum.web.controller;

import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;
import com.forum.forum.service.TopicCommentService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/topic/comment")
public class TopicCommentController {

    private static final Logger log = LoggerFactory.getLogger(TopicCommentController.class);

    private final TopicCommentService commentService;
    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public String addComment(@RequestParam Integer topicId,
                             @RequestParam String comment) {

        User user = userService.getCurrentUser();
        log.info("User {} adds comment to topic {}",
                user.getId(), topicId);

        commentService.add(topicId, user, comment);
        return "redirect:/topic/" + topicId;
    }

    @PreAuthorize("@commentSecurity.isOwner(#id)")
    @GetMapping("/edit/{id}")
    public String editCommentPage(@PathVariable Integer id, Model model) {

        log.debug("Open edit comment page, commentId={}", id);

        TopicComment comment = commentService.get(id);
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PreAuthorize("@commentSecurity.isOwner(#id)")
    @PostMapping("/edit/{id}")
    public String editComment(@PathVariable Integer id,
                              @RequestParam String text,
                              @RequestParam Integer topicId) {

        log.info("Edit comment {}, topic {}", id, topicId);

        commentService.update(id, text);
        return "redirect:/topic/" + topicId;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Integer id,
                                @RequestParam Integer topicId) {

        log.warn("Moderator/Admin deletes comment {}, topic {}",
                id, topicId);

        commentService.delete(id);
        return "redirect:/topic/" + topicId;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like/add")
    public String addLikeComment(@RequestParam Integer id,
                                 @RequestParam Integer topicId) {

        User user = userService.getCurrentUser();
        log.info("User {} likes comment {}", user.getLogin(), id);

        commentService.addLike(id, user);
        return "redirect:/topic/" + topicId;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like/delete")
    public String deleteLikeComment(@RequestParam Integer id,
                                    @RequestParam Integer topicId) {

        User user = userService.getCurrentUser();
        log.info("User {} removes like from comment {}", user.getLogin(), id);

        commentService.deleteLike(id, user);
        return "redirect:/topic/" + topicId;
    }
}

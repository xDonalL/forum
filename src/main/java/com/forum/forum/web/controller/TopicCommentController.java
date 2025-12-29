package com.forum.forum.web.controller;

import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.TopicCommentService;
import com.forum.forum.service.UserService;
import com.forum.forum.to.TopicCommentTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.forum.forum.model.ActionLog.DELETE_TOPIC;

@Controller
@RequiredArgsConstructor
@RequestMapping("/topic/comment")
public class TopicCommentController {

    private static final Logger log = LoggerFactory.getLogger(TopicCommentController.class);

    private final TopicCommentService commentService;
    private final UserService userService;
    private final AdminLogService adminLogService;
    private final TopicCommentService topicCommentService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public String addComment(@RequestParam Integer topicId,
                             @Valid @ModelAttribute("commentTo") TopicCommentTo commentTo,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        User user = userService.getCurrentUser();
        log.info("User {} adds comment to topic {}",
                user.getId(), topicId);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.commentTo",
                    bindingResult);
            redirectAttributes.addFlashAttribute("commentTo", commentTo);
            return "redirect:/topic/" + topicId;
        }

        commentService.add(topicId, user, commentTo.getComment());
        return "redirect:/topic/" + topicId;
    }

    @PreAuthorize("@commentSecurity.isOwner(#id)")
    @GetMapping("/edit/{id}")
    public String editCommentPage(@PathVariable Integer id, Model model) {

        log.debug("Open edit comment page, commentId={}", id);

        TopicComment comment = commentService.get(id);

        TopicCommentTo commentTo = new TopicCommentTo(
                comment.getId(),
                comment.getTopic().getId(),
                comment.getComment());

        model.addAttribute("commentTo", commentTo);
        return "comment-edit";
    }

    @PreAuthorize("@commentSecurity.isOwner(#id)")
    @PostMapping("/edit/{id}")
    public String editComment(@PathVariable Integer id,
                              @RequestParam Integer topicId,
                              @Valid @ModelAttribute("commentTo") TopicCommentTo commentTo,
                              BindingResult bindingResult) {

        log.info("Edit comment {}, topic {}", id, topicId);

        if (bindingResult.hasErrors()) {
            return "comment-edit";
        }

        commentService.update(id, commentTo.getComment());
        return "redirect:/topic/" + topicId;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable Integer id,
                                @RequestParam Integer topicId,
                                Authentication auth) {
        log.warn("Moderator/Admin deletes comment {}, topic {}",
                id, topicId);

        String authorLogin = topicCommentService.get(id).getAuthor().getLogin();

        commentService.delete(id);

        AuthorizedUser authorizedUser = (AuthorizedUser) auth.getPrincipal();
        adminLogService.logAction(authorizedUser.getUser().getLogin(),
                DELETE_TOPIC, authorLogin, id);
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

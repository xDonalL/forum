package com.forum.forum.web.controller;

import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.TopicService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.forum.forum.model.ActionLog.DELETE_TOPIC;

@Controller
@RequiredArgsConstructor
@RequestMapping("/topic")
public class TopicController {

    private static final Logger log = LoggerFactory.getLogger(TopicController.class);

    private final TopicService topicService;
    private final UserService userService;
    private final AdminLogService adminLogService;

    @GetMapping
    public String showTopics(@RequestParam(required = false) String sort,
                             @RequestParam(required = false) String q,
                             Model model) {

        log.debug("Show topics list: sort={}, query={}", sort, q);

        List<Topic> topics;

        if (q != null && !q.isBlank()) {
            log.info("Search topics by query: '{}'", q);
            topics = topicService.search(q);
        } else {
            topics = topicService.getAllSorted(sort);
        }

        model.addAttribute("topics", topics);
        return "topic/list";
    }

    @GetMapping("/{id}")
    public String showTopicPage(@PathVariable Integer id, Model model) {
        log.info("Open topic page: id={}", id);

        Topic topic = topicService.get(id);
        model.addAttribute("topic", topic);
        model.addAttribute("content", topic.getComments());

        return "topic/view";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/add")
    public String showCreateTopicPage() {
        log.debug("Open create topic page");
        return "topic/add";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public String addTopic(@RequestParam String title,
                           @RequestParam String content) {

        User user = userService.getCurrentUser();

        log.info("User {} creates topic with title='{}'",
                user.getId(), title);

        topicService.create(title, content, user);

        return "redirect:/topic";
    }

    @PreAuthorize("@topicSecurity.isOwner(#id)")
    @GetMapping("/edit/{id}")
    public String showEditTopicPage(@PathVariable Integer id, Model model) {

        log.info("Open edit topic page: topicId={}", id);

        Topic topic = topicService.get(id);
        model.addAttribute("topic", topic);

        return "topic/edit";
    }

    @PreAuthorize("@topicSecurity.isOwner(#id)")
    @PostMapping("/edit/{id}")
    public String editTopic(@PathVariable Integer id,
                            @RequestParam String title,
                            @RequestParam String content) {

        log.info("Update topic: id={}, newTitle='{}'", id, title);

        topicService.update(id, title, content);
        return "redirect:/topic/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/delete/{id}")
    public String deleteTopic(@PathVariable Integer id,
                              Authentication auth) {
        log.warn("Topic deleted: id={}", id);

        String authorLogin = topicService.get(id).getAuthor().getLogin();

        topicService.delete(id);

        AuthorizedUser authorizedUser = (AuthorizedUser) auth.getPrincipal();
        adminLogService.logAction(authorizedUser.getUser().getLogin(),
                DELETE_TOPIC, authorLogin, id);
        return "redirect:/topic";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like/add")
    public String addLikeTopic(@RequestParam Integer id) {

        User user = userService.getCurrentUser();

        log.info("User {} liked topic {}", user.getId(), id);

        topicService.addLike(id, user);
        return "redirect:/topic/" + id;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/like/delete")
    public String deleteLikeTopic(@RequestParam Integer id) {

        User user = userService.getCurrentUser();

        log.info("User {} removed like from topic {}", user.getId(), id);

        topicService.deleteLike(id, user);
        return "redirect:/topic/" + id;
    }
}

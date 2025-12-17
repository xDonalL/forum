package com.forum.forum.web.controller;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.service.ForumTopicService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumTopicController {

    private final ForumTopicService topicService;
    private final UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("topics", topicService.getAll());
        return "topic-list";
    }

    @GetMapping("/{id}")
    public String showTopicPage(@PathVariable Integer id, Model model) {
        ForumTopic topic = topicService.get(id);
        model.addAttribute("topic", topic);
        model.addAttribute("content", topic.getComments());
        return "topic-page";
    }

    @GetMapping("/add/topic")
    public String showCreateTopicPage(Model model) {
        return "add-topic-page";
    }


    @PreAuthorize("@topicSecurity.isOwner(#id)")
    @GetMapping("/topic/edit/{id}")
    public String showEditTopicPage(@PathVariable Integer id, Model model) {
        ForumTopic topic = topicService.get(id);
        model.addAttribute("topic", topic);
        return "edit-topic-page";
    }

    @PreAuthorize("@topicSecurity.isOwner(#id)")
    @PostMapping("/topic/edit/{id}")
    public String createTopic(@PathVariable Integer id,
                              @RequestParam String content) {
        topicService.update(id, content);
        return "redirect:/forum/" + id;
    }

    @PostMapping("/add/topic")
    public String createTopic(@RequestParam String title,
                              @RequestParam String content) {

        User user = userService.getCurrentUser();
        topicService.createTopic(title, content, user);

        return "redirect:/forum";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/topic/delete/{id}")
    public String deleteTopic(@PathVariable Integer id) {
        topicService.delete(id);
        return "redirect:/forum";
    }
}

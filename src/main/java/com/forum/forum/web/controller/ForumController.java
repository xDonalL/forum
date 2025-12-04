package com.forum.forum.web.controller;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.service.ForumMessageService;
import com.forum.forum.service.ForumTopicService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    private final ForumTopicService topicService;
    private final ForumMessageService messageService;
    private final UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("topics", topicService.getAll());
        return "topic-list";
    }

    @GetMapping("/{id}")
    public String topicPage(@PathVariable Integer id, Model model) {
        ForumTopic topic = topicService.get(id);
        model.addAttribute("topic", topic);
        model.addAttribute("messages", topic.getMessages());
        return "topic-page";
    }

    @GetMapping("/add/topic")
    public String topicPage(Model model) {
        return "add-topic-page";
    }

    @PostMapping("/add/topic")
    public String create(@RequestParam String title,
                         @RequestParam String description) {

        User user = userService.getCurrentUser();
        topicService.createTopic(title, description, user);

        return "redirect:/forum";
    }

    @PostMapping("/add/comment")
    public String addMessage(@RequestParam Integer topicId,
                             @RequestParam String content) {

        User user = userService.getCurrentUser();
        messageService.addMessage(topicId, user, content);

        return "redirect:/forum/" + topicId;
    }

}

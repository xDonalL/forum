package com.forum.forum.web.controller;

import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.service.TopicService;
import com.forum.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/topic")
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("topics", topicService.getAll());
        return "topic/list";
    }

    @GetMapping("/{id}")
    public String showTopicPage(@PathVariable Integer id, Model model) {
        Topic topic = topicService.get(id);
        model.addAttribute("topic", topic);
        model.addAttribute("content", topic.getComments());
        return "topic/view";
    }

    @GetMapping("/add")
    public String showCreateTopicPage(Model model) {
        return "topic/add";
    }


    @PostMapping("/add")
    public String editTopic(@RequestParam String title,
                            @RequestParam String content) {

        User user = userService.getCurrentUser();
        topicService.createTopic(title, content, user);

        return "redirect:/forum/topic";
    }

    @PreAuthorize("@topicSecurity.isOwner(#id)")
    @GetMapping("/edit/{id}")
    public String showEditTopicPage(@PathVariable Integer id, Model model) {
        Topic topic = topicService.get(id);
        model.addAttribute("topic", topic);
        return "topic/edit";
    }

    @PreAuthorize("@topicSecurity.isOwner(#id)")
    @PostMapping("/edit/{id}")
    public String editTopic(@PathVariable Integer id,
                            @RequestParam String content) {
        topicService.update(id, content);
        return "redirect:/forum/topic/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/delete/{id}")
    public String deleteTopic(@PathVariable Integer id) {
        topicService.delete(id);
        return "redirect:/forum/topic";
    }
}

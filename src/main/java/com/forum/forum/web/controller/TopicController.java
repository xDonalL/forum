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

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;
    private final UserService userService;

    @GetMapping
    public String showTopics(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String q,
            Model model) {

        List<Topic> topics;

        if (q != null && !q.isBlank()) {
            topics = topicService.search(q);
        } else {
            topics = topicService.getAllSorted(sort);
        }

        model.addAttribute("topics", topics);

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
        topicService.create(title, content, user);

        return "redirect:/topic";
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
        return "redirect:/topic/" + id;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/delete/{id}")
    public String deleteTopic(@PathVariable Integer id) {
        topicService.delete(id);
        return "redirect:/topic";
    }

    @PostMapping("/like/add")
    public String addLikeTopic(@RequestParam Integer id) {
        User user = userService.getCurrentUser();
        topicService.addLike(id, user);
        return "redirect:/topic/" + id;
    }

    @PostMapping("/like/delete")
    public String deleteLikeTopic(@RequestParam Integer id) {
        User user = userService.getCurrentUser();
        topicService.deleteLike(id, user);
        return "redirect:/topic/" + id;
    }
}

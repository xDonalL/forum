package com.forum.forum.web.controller;

import com.forum.forum.dto.TopicPageDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import com.forum.forum.model.TopicSort;
import com.forum.forum.model.User;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.TopicService;
import com.forum.forum.service.UserService;
import com.forum.forum.to.TopicCommentTo;
import com.forum.forum.to.TopicTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String showTopics(@RequestParam(required = false) TopicSort sort,
                             @RequestParam(required = false) String q,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {

        log.debug("Show topics list: sort={}, query={}", sort, q);

        Page<TopicPagesDto> topics;

        if (q != null && !q.isBlank()) {
            log.info("Search topics by query: '{}'", q);
            topics = topicService.search(page, 10, q);
        } else {
            topics = topicService.getAllSorted(page, 10, sort);
        }

        model.addAttribute("topics", topics);
        model.addAttribute("baseUrl", "/topic");
        model.addAttribute("sort", sort);

        return "topic/list";
    }

    @GetMapping("/{id}")
    public String showTopicPage(@PathVariable Integer id,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {
        log.info("Open topic page: id={}", id);

        TopicPageDto topicPage = topicService.getTopicView(page, 10, id);

        model.addAttribute("topic", topicPage.topic());
        model.addAttribute("comments", topicPage.comments());
        model.addAttribute("baseUrl", "/topic/" + id);

        if (!model.containsAttribute("commentTo")) {
            TopicCommentTo commentTo = new TopicCommentTo();
            model.addAttribute("commentTo", commentTo);
        }

        return "topic/view";
    }

    @GetMapping("/add")
    public String showCreateTopicPage(Model model) {
        log.debug("Open create topic page");
        model.addAttribute("topicTo", new TopicTo());
        model.addAttribute("mode", "create");
        return "topic/form";
    }

    @PostMapping("/add")
    public String addTopic(@Valid @ModelAttribute("topicTo") TopicTo topicTo,
                           BindingResult bindingResult,
                           Model model) {

        User user = userService.getCurrentUser();

        log.info("User {} creates topic with title='{}'",
                user.getId(), topicTo.getTitle());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "topic/form";
        }

        topicService.create(topicTo.getTitle(), topicTo.getContent(), user);

        return "redirect:/topic";
    }

    @GetMapping("/edit/{id}")
    public String showEditTopicPage(@PathVariable Integer id, Model model) {

        log.info("Open edit topic page: topicId={}", id);

        Topic topic = topicService.get(id);
        TopicTo topicTo = new TopicTo(
                topic.getId(),
                topic.getTitle(),
                topic.getContent());

        model.addAttribute("topicTo", topicTo);
        model.addAttribute("mode", "update");

        return "topic/form";
    }

    @PostMapping("/edit/{id}")
    public String editTopic(@PathVariable Integer id,
                            @Valid @ModelAttribute("topicTo") TopicTo topicTo,
                            BindingResult bindingResult,
                            Model model) {

        log.info("Update topic: id={}, newTitle='{}'", id, topicTo.getTitle());

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "update");
            return "topic/form";
        }

        topicService.update(id, topicTo.getTitle(), topicTo.getContent());
        return "redirect:/topic/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteTopic(@PathVariable Integer id) {
        log.warn("Topic deleted: id={}", id);

        String authorLogin = topicService.get(id).getAuthor().getLogin();

        topicService.delete(id);

        User user = userService.getCurrentUser();
        adminLogService.logAction(user.getLogin(), DELETE_TOPIC, authorLogin, id);
        return "redirect:/topic";
    }

    @PostMapping("/like")
    private String toggleLikeComment(@RequestParam Integer topicId) {
        log.info("likes comment {}", topicId);

        topicService.toggleLike(topicId);
        return "redirect:/topic/" + topicId;
    }
}

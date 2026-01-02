package com.forum.forum.web.controller;

import com.forum.forum.readmodel.TopicListView;
import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.TopicService;
import com.forum.forum.service.UserService;
import com.forum.forum.to.TopicCommentTo;
import com.forum.forum.to.TopicTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

        List<TopicListView> topics;

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
        return "topic/add";
    }

    @PostMapping("/add")
    public String addTopic(@Valid @ModelAttribute("topicTo") TopicTo topicTo,
                           BindingResult bindingResult) {

        User user = userService.getCurrentUser();

        log.info("User {} creates topic with title='{}'",
                user.getId(), topicTo.getTitle());

        if (bindingResult.hasErrors()) {
            return "topic/add";
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

        return "topic/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTopic(@PathVariable Integer id,
                            @Valid @ModelAttribute("topicTo") TopicTo topicTo,
                            BindingResult bindingResult) {

        log.info("Update topic: id={}, newTitle='{}'", id, topicTo.getTitle());

        if (bindingResult.hasErrors()) {
            return "topic/edit";
        }

        topicService.update(id, topicTo.getTitle(), topicTo.getContent());
        return "redirect:/topic/" + id;
    }

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

    @PostMapping("/like/add")
    public String addLikeTopic(@RequestParam Integer id) {

        User user = userService.getCurrentUser();

        log.info("User {} liked topic {}", user.getId(), id);

        topicService.addLike(id, user);
        return "redirect:/topic/" + id;
    }

    @PostMapping("/like/delete")
    public String deleteLikeTopic(@RequestParam Integer id) {

        User user = userService.getCurrentUser();

        log.info("User {} removed like from topic {}", user.getId(), id);

        topicService.deleteLike(id, user);
        return "redirect:/topic/" + id;
    }
}

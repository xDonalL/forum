package com.forum.forum.service;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.CrudForumTopicRepository;
import com.forum.forum.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumTopicService {

    private final CrudForumTopicRepository topicRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createTopic(String title, String description, User user) {
        ForumTopic topic = new ForumTopic();
        topic.setTitle(title);
        topic.setContent(description);
        topic.setAuthor(user);
        topicRepository.save(topic);
    }

    public List<ForumTopic> getAll() {
        return topicRepository.findAll();
    }

    public ForumTopic get(Integer id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));
    }
}
package com.forum.forum.service;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaForumTopicRepository;
import com.forum.forum.util.ValidUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumTopicService {

    private final DataJpaForumTopicRepository topicRepository;

    @Transactional
    public ForumTopic createTopic(String title, String content, User user) {
        ForumTopic topic = new ForumTopic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setAuthor(user);
        return topicRepository.save(topic);
    }

    public List<ForumTopic> getAll() {
        return topicRepository.getAll();
    }

    public ForumTopic get(Integer id) {
        return ValidUtil.checkNotFound(topicRepository.get(id), "topic with id= " + id + " not exist");
    }
}
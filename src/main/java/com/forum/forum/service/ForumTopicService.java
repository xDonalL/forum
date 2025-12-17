package com.forum.forum.service;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaForumCommentRepository;
import com.forum.forum.repository.forum.DataJpaForumTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class ForumTopicService {

    private final DataJpaForumTopicRepository topicRepository;
    private final DataJpaForumCommentRepository commentRepository;

    @Transactional
    public ForumTopic createTopic(String title, String content, User user) {
        ForumTopic topic = new ForumTopic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setAuthor(user);
        return topicRepository.save(topic);
    }

    public ForumTopic update(Integer id, String content) {
        ForumTopic topic = topicRepository.get(id);
        checkNotFound(topic, "topic with id= " + id + " not exist");
        topic.setContent(content);
        return topicRepository.save(topic);
    }

    public List<ForumTopic> getAll() {
        return topicRepository.getAll();
    }

    public ForumTopic get(Integer id) {
        return checkNotFound(topicRepository.get(id), "topic with id= " + id + " not exist");
    }

    @Transactional
    public boolean delete(Integer id) {
        checkNotFound(topicRepository.get(id), "topic with id= " + id + " not exist");
        commentRepository.deleteByTopicId(id);
        return topicRepository.delete(id);
    }
}
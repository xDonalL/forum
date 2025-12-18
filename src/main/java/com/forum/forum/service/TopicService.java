package com.forum.forum.service;

import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicCommentRepository;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final DataJpaTopicRepository topicRepository;
    private final DataJpaTopicCommentRepository commentRepository;

    @Transactional
    public Topic createTopic(String title, String content, User user) {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setAuthor(user);
        return topicRepository.save(topic);
    }

    public Topic update(Integer id, String content) {
        Topic topic = topicRepository.get(id);
        checkNotFound(topic, "topic with id= " + id + " not exist");
        topic.setContent(content);
        return topicRepository.save(topic);
    }

    public List<Topic> getAll() {
        return topicRepository.getAll();
    }

    public Topic get(Integer id) {
        return checkNotFound(topicRepository.get(id), "topic with id= " + id + " not exist");
    }

    @Transactional
    public boolean delete(Integer id) {
        checkNotFound(topicRepository.get(id), "topic with id= " + id + " not exist");
        commentRepository.deleteByTopicId(id);
        return topicRepository.delete(id);
    }
}
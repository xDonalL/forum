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
    public Topic create(String title, String content, User user) {
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

    public Topic get(Integer id) {
        return checkNotFound(topicRepository.get(id), "topic with id= " + id + " not exist");
    }

    @Transactional
    public boolean delete(Integer id) {
        checkNotFound(topicRepository.get(id), "topic with id= " + id + " not exist");
        commentRepository.deleteByTopicId(id);
        return topicRepository.delete(id);
    }

    @Transactional
    public boolean addLike(Integer topicId, User user) {
        Topic topic = topicRepository.get(topicId);
        checkNotFound(topic, "topic with id= " + topicId + " not exist");
        return topic.getLikedUsers().add(user);
    }

    @Transactional
    public boolean deleteLike(Integer topicId, User user) {
        Topic topic = topicRepository.get(topicId);
        checkNotFound(topic, "topic with id= " + topicId + " not exist");
        return topic.getLikedUsers().remove(user);
    }

    public List<Topic> getAllSorted(String sort) {
        List<Topic> topics;

        if (sort == null) {
            return topics = topicRepository.getAll();
        }
        switch (sort) {
            case "dateAsc":
                topics = topicRepository.getTopicsByDateAsc();
                break;
            case "likes":
                topics = topicRepository.getTopicsByLikes();
                break;
            default:
                topics = topicRepository.getAll();
        }
        return topics;
    }

    public List<Topic> search(String q) {
        return checkNotFound(topicRepository.getTopicsByTopicName(q), "topic with q= " + q + " not exist");
    }
}
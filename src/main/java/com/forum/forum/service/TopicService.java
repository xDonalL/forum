package com.forum.forum.service;

import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicCommentRepository;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class TopicService {

    private static final Logger log = LoggerFactory.getLogger(TopicService.class);

    private final DataJpaTopicRepository topicRepository;
    private final DataJpaTopicCommentRepository commentRepository;

    @Transactional
    public Topic create(String title, String content, User user) {
        log.debug("Creating topic: title='{}', authorId={}", title, user.getId());

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setContent(content);
        topic.setAuthor(user);

        Topic saved = topicRepository.save(topic);

        log.info("Topic created: topicId={}, authorId={}", saved.getId(), user.getId());
        return saved;
    }

    @Transactional
    public Topic update(Integer id, String title, String content) {
        log.debug("Updating topic: id={}", id);

        Topic topic = topicRepository.get(id);
        checkNotFound(topic, "topic with id=" + id + " not exist");

        topic.setTitle(title);
        topic.setContent(content);

        Topic updated = topicRepository.save(topic);

        log.info("Topic updated: topicId={}", id);
        return updated;
    }

    public Topic get(Integer id) {
        log.debug("Getting topic: id={}", id);
        return checkNotFound(
                topicRepository.get(id),
                "topic with id=" + id + " not exist"
        );
    }

    @Transactional
    public boolean delete(Integer id) {
        log.debug("Deleting topic: id={}", id);

        Topic topic = topicRepository.get(id);
        checkNotFound(topic, "topic with id=" + id + " not exist");

        commentRepository.deleteByTopicId(id);
        boolean deleted = topicRepository.delete(id);

        log.info("Topic deleted: topicId={}", id);
        return deleted;
    }

    @Transactional
    public boolean addLike(Integer topicId, User user) {
        log.debug("Adding like: topicId={}, userId={}", topicId, user.getId());

        Topic topic = topicRepository.get(topicId);
        checkNotFound(topic, "topic with id=" + topicId + " not exist");

        boolean added = topic.getLikedUsers().add(user);

        if (added) {
            log.info("Topic liked: topicId={}, userId={}", topicId, user.getId());
        } else {
            log.debug("Like already exists: topicId={}, userId={}", topicId, user.getId());
        }

        return added;
    }

    @Transactional
    public boolean deleteLike(Integer topicId, User user) {
        log.debug("Removing like: topicId={}, userId={}", topicId, user.getId());

        Topic topic = topicRepository.get(topicId);
        checkNotFound(topic, "topic with id=" + topicId + " not exist");

        boolean removed = topic.getLikedUsers().remove(user);

        if (removed) {
            log.info("Like removed: topicId={}, userId={}", topicId, user.getId());
        }

        return removed;
    }

    public List<Topic> getAllSorted(String sort) {
        log.debug("Getting topics sorted by '{}'", sort);

        if (sort == null) {
            return topicRepository.getAll();
        }

        return switch (sort) {
            case "dateAsc" -> topicRepository.getTopicsByDateAsc();
            case "likes" -> topicRepository.getTopicsByLikes();
            default -> topicRepository.getAll();
        };
    }

    public List<Topic> search(String q) {
        log.debug("Searching topics: query='{}'", q);

        List<Topic> result = topicRepository.getTopicsByTopicName(q);
        checkNotFound(result, "topic with q=" + q + " not exist");

        log.info("Search completed: query='{}', found={}", q, result.size());
        return result;
    }
}
package com.forum.forum.repository.forum;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJpaForumTopicRepository implements BaseRepository<ForumTopic> {

    public DataJpaForumTopicRepository(CrudForumTopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    private final CrudForumTopicRepository topicRepository;


    @Override
    public ForumTopic save(ForumTopic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public boolean delete(int id) {
        return topicRepository.delete(id) != 0;
    }

    @Override
    public ForumTopic get(int id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Override
    public List<ForumTopic> getAll() {
        return topicRepository.findAll();
    }
}
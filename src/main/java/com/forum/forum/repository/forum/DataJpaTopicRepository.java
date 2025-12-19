package com.forum.forum.repository.forum;

import com.forum.forum.model.Topic;
import com.forum.forum.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJpaTopicRepository implements BaseRepository<Topic> {

    public DataJpaTopicRepository(CrudTopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    private final CrudTopicRepository topicRepository;


    @Override
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public boolean delete(int id) {
        return topicRepository.delete(id) != 0;
    }

    @Override
    public Topic get(int id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Override
    public List<Topic> getAll() {
        return topicRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Topic> getTopicsByLikes() {
        return topicRepository.findAllByOrderByLikedUsersAsc();
    }

    public List<Topic> getTopicsByDateAsc() {
        return topicRepository.findAllByOrderByCreatedAtAsc();
    }

    public List<Topic> getTopicsByTopicName(String topicName) {
        return topicRepository.findByTitleContainingIgnoreCase(topicName);
    }
}
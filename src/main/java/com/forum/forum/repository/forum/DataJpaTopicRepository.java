package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import com.forum.forum.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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

    public TopicDto getDetails(int id) {
        return topicRepository.findTopicDetails(id);
    }

    public Page<TopicPagesDto> getAllTopics(Pageable pageable) {
        return topicRepository.findAllDecs(pageable);
    }

    public Page<TopicPagesDto> getAllTopicsAsc(Pageable pageable) {
        return topicRepository.findAllAsc(pageable);
    }

    public Page<TopicPagesDto> getTopicsByTitle(String topicName, Pageable pageable) {
        return topicRepository.findByTitle(topicName, pageable);
    }

    public Page<TopicPagesDto> getTopicsSortByLikes(Pageable pageable) {
        return topicRepository.findByLikes(pageable);
    }
}
package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DataJpaTopicRepository implements TopicRepository {

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
    public TopicDto getDetails(int id, Integer userId) {
        return topicRepository.findTopicDetails(id, userId);
    }

    @Override
    public Page<TopicPagesDto> getAllTopics(Pageable pageable) {
        return topicRepository.findAllPage(pageable);
    }

    @Override
    public Page<TopicPagesDto> getTopicsByTitle(String topicName, Pageable pageable) {
        return topicRepository.findByTitle(topicName, pageable);
    }

    @Override
    public Page<TopicPagesDto> getTopicsSortByLikes(Pageable pageable) {
        return topicRepository.findByLikes(pageable);
    }
}
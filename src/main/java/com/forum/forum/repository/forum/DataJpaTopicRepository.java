package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import com.forum.forum.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DataJpaTopicRepository implements BaseRepository<Topic> {

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

    public TopicDto getDetails(int id, Integer userId) {
        return topicRepository.findTopicDetails(id, userId);
    }

    public Page<TopicPagesDto> getAllTopics(Pageable pageable) {
        return topicRepository.findAllDesc(pageable);
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
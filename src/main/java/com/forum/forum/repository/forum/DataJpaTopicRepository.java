package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.model.Topic;
import com.forum.forum.readmodel.TopicListView;
import com.forum.forum.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJpaTopicRepository implements BaseRepository<Topic> {

    public DataJpaTopicRepository(CrudTopicRepository topicRepository,
                                  TopicListViewRepository topicListViewRepository) {
        this.topicRepository = topicRepository;
        this.topicListViewRepository = topicListViewRepository;
    }

    private final CrudTopicRepository topicRepository;
    private final TopicListViewRepository topicListViewRepository;


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

    public List<TopicListView> getAll() {
        return topicListViewRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<TopicListView> getList() {
        return topicListViewRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<TopicListView> getTopicsByLikes() {
        return topicListViewRepository.findAllByOrderByLikesCountDesc();
    }

    public List<TopicListView> getTopicsByDateAsc() {
        return topicListViewRepository.findAllByOrderByCreatedAtAsc();
    }

    public List<TopicListView> getTopicsByTopicName(String topicName) {
        return topicListViewRepository.findByTitleContainingIgnoreCase(topicName);
    }
}
package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import com.forum.forum.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicRepository extends BaseRepository<Topic> {

    TopicDto getDetails(int id, Integer userId);

    Page<TopicPagesDto> getAllTopics(Pageable pageable);

    Page<TopicPagesDto> getAllTopicsAsc(Pageable pageable);

    Page<TopicPagesDto> getTopicsByTitle(String topicName, Pageable pageable);

    Page<TopicPagesDto> getTopicsSortByLikes(Pageable pageable);
}

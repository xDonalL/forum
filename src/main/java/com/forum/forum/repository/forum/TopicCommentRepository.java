package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicCommentDto;
import com.forum.forum.model.TopicComment;
import com.forum.forum.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TopicCommentRepository extends BaseRepository<TopicComment> {

    int deleteByTopicId(int topicId);

    Page<TopicCommentDto> getPageCommentByTopic(Pageable pageable, int id, int userId);
}

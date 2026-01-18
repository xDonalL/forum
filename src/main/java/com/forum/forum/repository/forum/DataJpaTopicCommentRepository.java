package com.forum.forum.repository.forum;

import com.forum.forum.dto.TopicCommentDto;
import com.forum.forum.model.TopicComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DataJpaTopicCommentRepository implements TopicCommentRepository {

    private final CrudTopicCommentRepository commentRepository;

    @Override
    public TopicComment save(TopicComment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public boolean delete(int id) {
        return commentRepository.delete(id) != 0;
    }

    @Override
    public int deleteByTopicId(int topicId) {
        return commentRepository.deleteByTopicId(topicId);
    }

    @Override
    public TopicComment get(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Page<TopicCommentDto> getPageCommentByTopic(Pageable pageable, int id, int userId) {
        return commentRepository.findCommentsByTopicId(pageable, id, userId);
    }
}
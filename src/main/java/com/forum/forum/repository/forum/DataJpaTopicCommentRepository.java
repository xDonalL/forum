package com.forum.forum.repository.forum;

import com.forum.forum.model.TopicComment;
import com.forum.forum.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJpaTopicCommentRepository implements BaseRepository<TopicComment> {

    public DataJpaTopicCommentRepository(CrudTopicCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    private final CrudTopicCommentRepository commentRepository;

    @Override
    public TopicComment save(TopicComment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public boolean delete(int id) {
        return commentRepository.delete(id) != 0;
    }

    public int deleteByTopicId(int topicId) {
        return commentRepository.deleteByTopicId(topicId);
    }

    @Override
    public TopicComment get(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<TopicComment> getAll() {
        return commentRepository.findAll();
    }
}
package com.forum.forum.repository.forum;

import com.forum.forum.model.ForumComment;
import com.forum.forum.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJpaForumCommentRepository implements BaseRepository<ForumComment> {

    public DataJpaForumCommentRepository(CrudForumCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    private final CrudForumCommentRepository commentRepository;

    @Override
    public ForumComment save(ForumComment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public boolean delete(int id) {
        return commentRepository.delete(id) != 0;
    }

    @Override
    public ForumComment get(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<ForumComment> getAll() {
        return commentRepository.findAll();
    }
}
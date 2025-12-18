package com.forum.forum.service;

import com.forum.forum.model.TopicComment;
import com.forum.forum.model.Topic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicCommentRepository;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class TopicCommentService {

    private final DataJpaTopicCommentRepository commentRepository;
    private final DataJpaTopicRepository topicRepository;

    @Transactional
    public TopicComment addComment(Integer topicId, User author, String content) {
        Topic topic = topicRepository.get(topicId);

        TopicComment msg = new TopicComment();
        msg.setTopic(topic);
        msg.setAuthor(author);
        msg.setComment(content);

        return commentRepository.save(msg);
    }

    @Transactional
    public boolean delete(Integer id) {
        checkNotFound(commentRepository.get(id), "comment with id= " + id + " not exist");
        return commentRepository.delete(id);
    }

    public TopicComment get(Integer id) {
        return checkNotFound(commentRepository.get(id), "comment with id= " + id + " not exist");
    }

    public TopicComment update(Integer id, String text) {
        TopicComment comment = commentRepository.get(id);
        checkNotFound(comment, "comment with id= " + id + " not exist");
        comment.setComment(text);
        return commentRepository.save(comment);
    }

    @Transactional
    public boolean addLike(Integer commentId, User user) {
        TopicComment comment = commentRepository.get(commentId);
        checkNotFound(comment, "comment with id= " + commentId + " not exist");
        return comment.getLikedUsers().add(user);
    }

    @Transactional
    public boolean deleteLike(Integer commentId, User user) {
        TopicComment comment = commentRepository.get(commentId);
        checkNotFound(comment, "comment with id= " + commentId + " not exist");
        return comment.getLikedUsers().remove(user);
    }
}
package com.forum.forum.service;

import com.forum.forum.model.Topic;
import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicCommentRepository;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class TopicCommentService {

    private static final Logger log = LoggerFactory.getLogger(TopicCommentService.class);

    private final DataJpaTopicCommentRepository commentRepository;
    private final DataJpaTopicRepository topicRepository;

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public TopicComment add(Integer topicId, User author, String content) {
        log.debug("Adding comment: topicId={}, authorId={}", topicId, author.getId());

        Topic topic = topicRepository.get(topicId);
        checkNotFound(topic, "topic with id=" + topicId + " not exist");

        TopicComment comment = new TopicComment();
        comment.setTopic(topic);
        comment.setAuthor(author);
        comment.setComment(content);

        TopicComment saved = commentRepository.save(comment);

        log.info("Comment created: commentId={}, topicId={}, authorId={}",
                saved.getId(), topicId, author.getId());

        return saved;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @Transactional
    public boolean delete(Integer id) {
        log.debug("Deleting comment: commentId={}", id);

        TopicComment comment = commentRepository.get(id);
        checkNotFound(comment, "comment with id=" + id + " not exist");

        boolean deleted = commentRepository.delete(id);

        log.info("Comment deleted: commentId={}, authorId={}",
                id, comment.getAuthor().getId());

        return deleted;
    }

    public TopicComment get(Integer id) {
        log.debug("Getting comment: commentId={}", id);

        return checkNotFound(commentRepository.get(id),
                "comment with id=" + id + " not exist");
    }

    @PreAuthorize("@topicSecurity.isOwner(#id)")
    public TopicComment update(Integer id, String text) {
        log.debug("Updating comment: commentId={}", id);

        TopicComment comment = commentRepository.get(id);
        checkNotFound(comment, "comment with id=" + id + " not exist");

        comment.setComment(text);

        TopicComment updated = commentRepository.save(comment);

        log.info("Comment updated: commentId={}, authorId={}",
                id, updated.getAuthor().getId());

        return updated;
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public boolean addLike(Integer commentId, User user) {
        log.debug("Adding like to comment: commentId={}, userId={}",
                commentId, user.getId());

        TopicComment comment = commentRepository.get(commentId);
        checkNotFound(comment, "comment with id=" + commentId + " not exist");

        boolean added = comment.getLikedUsers().add(user);

        if (added) {
            log.info("Comment liked: commentId={}, userId={}",
                    commentId, user.getId());
        }

        return added;
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public boolean deleteLike(Integer commentId, User user) {
        log.debug("Removing like from comment: commentId={}, userId={}",
                commentId, user.getId());

        TopicComment comment = commentRepository.get(commentId);
        checkNotFound(comment, "comment with id=" + commentId + " not exist");

        boolean removed = comment.getLikedUsers().remove(user);

        if (removed) {
            log.info("Comment like removed: commentId={}, userId={}",
                    commentId, user.getId());
        }

        return removed;
    }
}
package com.forum.forum.service;

import com.forum.forum.model.ForumComment;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaForumCommentRepository;
import com.forum.forum.repository.forum.DataJpaForumTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
@RequiredArgsConstructor
public class ForumCommentService {

    private final DataJpaForumCommentRepository commentRepository;
    private final DataJpaForumTopicRepository topicRepository;

    @Transactional
    public ForumComment addComment(Integer topicId, User author, String content) {
        ForumTopic topic = topicRepository.get(topicId);

        ForumComment msg = new ForumComment();
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
}
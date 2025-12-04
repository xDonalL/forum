package com.forum.forum.service;

import com.forum.forum.model.ForumComment;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.CrudForumCommentRepository;
import com.forum.forum.repository.forum.CrudForumTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ForumMessageService {

    private final CrudForumCommentRepository messageRepository;
    private final CrudForumTopicRepository topicRepository;

    @Transactional
    public void addMessage(Integer topicId, User author, String content) {
        ForumTopic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        ForumComment msg = new ForumComment();
        msg.setTopic(topic);
        msg.setAuthor(author);
        msg.setComment(content);

        messageRepository.save(msg);
    }
}
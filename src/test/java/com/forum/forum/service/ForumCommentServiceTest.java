package com.forum.forum.service;

import com.forum.forum.ForumTestData;
import com.forum.forum.model.ForumComment;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.CrudForumCommentRepository;
import com.forum.forum.repository.forum.CrudForumTopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ForumCommentServiceTest {

    private CrudForumCommentRepository commentRepository;
    private CrudForumTopicRepository topicRepository;
    private ForumCommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CrudForumCommentRepository.class);
        topicRepository = mock(CrudForumTopicRepository.class);
        commentService = new ForumCommentService(commentRepository, topicRepository);
    }

    @Test
    void addComment() {
        User author = ForumTestData.COMMENT1.getAuthor();
        ForumTopic topic = ForumTestData.COMMENT1.getTopic();
        ForumComment newComment = ForumTestData.getNewComment(author, topic);

        when(topicRepository.findById(topic.getId()))
                .thenReturn(Optional.of(topic));
        when(commentRepository.save(any(ForumComment.class)))
                .thenReturn(newComment);

        ForumComment saved = commentService.addComment(topic.getId(), author, newComment.getComment());

        assertNotNull(saved);
        assertEquals(newComment.getComment(), saved.getComment());
        assertEquals(author, saved.getAuthor());
        assertEquals(topic, saved.getTopic());

        ArgumentCaptor<ForumComment> captor = ArgumentCaptor.forClass(ForumComment.class);
        verify(commentRepository).save(captor.capture());
        assertEquals(newComment.getComment(), captor.getValue().getComment());
    }
}
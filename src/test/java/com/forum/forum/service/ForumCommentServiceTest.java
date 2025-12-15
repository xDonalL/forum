package com.forum.forum.service;

import com.forum.forum.ForumTestData;
import com.forum.forum.model.ForumComment;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaForumCommentRepository;
import com.forum.forum.repository.forum.DataJpaForumTopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ForumCommentServiceTest {

    private DataJpaForumCommentRepository commentRepository;
    private DataJpaForumTopicRepository topicRepository;
    private ForumCommentService commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(DataJpaForumCommentRepository.class);
        topicRepository = mock(DataJpaForumTopicRepository.class);
        commentService = new ForumCommentService(commentRepository, topicRepository);
    }

    @Test
    void addCommentSuccess() {
        User author = ForumTestData.COMMENT1.getAuthor();
        ForumTopic topic = ForumTestData.COMMENT1.getTopic();
        ForumComment newComment = ForumTestData.getNewComment(author, topic);

        when(topicRepository.get(topic.getId())).thenReturn(topic);
        when(commentRepository.save(any(ForumComment.class))).thenReturn(newComment);

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
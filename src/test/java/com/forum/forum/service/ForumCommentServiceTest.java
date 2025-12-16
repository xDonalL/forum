package com.forum.forum.service;

import com.forum.forum.ForumTestData;
import com.forum.forum.model.ForumComment;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaForumCommentRepository;
import com.forum.forum.repository.forum.DataJpaForumTopicRepository;
import com.forum.forum.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.forum.forum.ForumTestData.COMMENT1;
import static com.forum.forum.ForumTestData.COMMENT1_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ForumCommentServiceTest {

    @Mock
    private DataJpaForumCommentRepository commentRepository;

    @Mock
    private DataJpaForumTopicRepository topicRepository;

    @InjectMocks
    private ForumCommentService commentService;

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

    @Test
    void deleteCommentSuccess() {
        when(commentRepository.get(COMMENT1_ID)).thenReturn(COMMENT1);
        when(commentRepository.delete(COMMENT1_ID)).thenReturn(true);

        boolean deleted = commentService.delete(COMMENT1_ID);

        assertTrue(deleted);
        verify(commentRepository).get(COMMENT1_ID);
        verify(commentRepository).delete(COMMENT1_ID);
    }

    @Test
    void deleteCommentNotFoundException() {
        when(commentRepository.get(COMMENT1_ID)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> commentService.delete(COMMENT1_ID));
    }
}
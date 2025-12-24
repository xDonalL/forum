package com.forum.forum.service;

import com.forum.forum.model.Topic;
import com.forum.forum.model.TopicComment;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.DataJpaTopicCommentRepository;
import com.forum.forum.repository.forum.DataJpaTopicRepository;
import com.forum.forum.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.forum.forum.ForumCommentTestData.*;
import static com.forum.forum.UserTestData.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TopicCommentServiceTest {

    @Mock
    private DataJpaTopicCommentRepository commentRepository;

    @Mock
    private DataJpaTopicRepository topicRepository;

    @InjectMocks
    private TopicCommentService commentService;

    @Test
    void addCommentSuccess() {
        User author = COMMENT1.getAuthor();
        Topic topic = COMMENT1.getTopic();
        TopicComment newComment = getNewComment(author, topic);

        when(topicRepository.get(topic.getId())).thenReturn(topic);
        when(commentRepository.save(any(TopicComment.class))).thenReturn(newComment);

        TopicComment saved = commentService.add(topic.getId(), author, newComment.getComment());

        assertNotNull(saved);
        assertEquals(newComment.getComment(), saved.getComment());
        assertEquals(author, saved.getAuthor());
        assertEquals(topic, saved.getTopic());

        ArgumentCaptor<TopicComment> captor = ArgumentCaptor.forClass(TopicComment.class);
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

    @Test
    void updateCommentSuccess() {
        String newComment = "updated comment";

        when(commentRepository.get(COMMENT1_ID)).thenReturn(COMMENT1);
        when(commentRepository.save(any(TopicComment.class))).thenReturn(COMMENT1);

        TopicComment updated = commentService.update(COMMENT1_ID, newComment);

        assertEquals(newComment, updated.getComment());
        verify(commentRepository).get(COMMENT1_ID);
        verify(commentRepository).save(COMMENT1);
    }

    @Test
    void updateCommentNotFoundException() {
        when(commentRepository.get(COMMENT1_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> commentService.update(COMMENT1_ID, COMMENT1.getComment()));
    }

    @Test
    void addLikeSuccess() {
        when(commentRepository.get(COMMENT1_ID)).thenReturn(COMMENT1);

        assertTrue(commentService.addLike(COMMENT1_ID, USER));
    }

    @Test
    void addLikeNotFoundException() {
        when(commentRepository.get(COMMENT1_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> commentService.addLike(COMMENT1_ID, USER));
    }

    @Test
    void deleteLikeSuccess() {
        when(commentRepository.get(COMMENT1_ID)).thenReturn(COMMENT1);
        commentService.addLike(COMMENT1_ID, USER);

        assertTrue(commentService.deleteLike(COMMENT1_ID, USER));
    }

    @Test
    void deleteLikeNotFoundException() {
        when(commentRepository.get(COMMENT1_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> commentService.deleteLike(COMMENT1_ID, USER));
    }
}
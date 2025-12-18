package com.forum.forum.service;

import com.forum.forum.UserTestData;
import com.forum.forum.model.Topic;
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

import java.util.List;

import static com.forum.forum.ForumTopicTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private DataJpaTopicRepository topicRepository;

    @Mock
    private DataJpaTopicCommentRepository commentRepository;

    @InjectMocks
    private TopicService topicService;

    @Test
    void createTopicSuccess() {
        User author = TOPIC1.getAuthor();
        Topic newTopic = getNewTopic(author);

        when(topicRepository.save(any(Topic.class)))
                .thenReturn(newTopic);

        Topic savedTopic = topicService.createTopic(
                newTopic.getTitle(),
                newTopic.getContent(),
                UserTestData.USER);

        assertNotNull(savedTopic);
        assertEquals(newTopic.getTitle(), savedTopic.getTitle());
        assertEquals(author, savedTopic.getAuthor());


        ArgumentCaptor<Topic> captor = ArgumentCaptor.forClass(Topic.class);
        verify(topicRepository).save(captor.capture());
        assertEquals(savedTopic.getTitle(), captor.getValue().getTitle());
    }

    @Test
    void getAllTopicSuccess() {
        when(topicRepository.getAll()).thenReturn(ALL_TOPIC);

        List<Topic> topics = topicService.getAll();

        assertEquals(ALL_TOPIC.size(), topics.size());
        assertTrue(topics.contains(TOPIC1));
        assertTrue(topics.contains(TOPIC2));
    }

    @Test
    void getTopicSuccess() {
        when(topicRepository.get(TOPIC1_ID)).thenReturn(TOPIC1);

        Topic topic = topicService.get(TOPIC1_ID);
        assertNotNull(topic);
        assertEquals(TOPIC1.getId(), topic.getId());
    }

    @Test
    void getTopicNotFound() {
        when(topicRepository.get(anyInt())).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> topicService.get(TOPIC1_ID));
    }

    @Test
    void deleteTopicSuccess() {
        when(topicRepository.get(TOPIC1_ID)).thenReturn(TOPIC1);
        when(topicRepository.delete(TOPIC1_ID)).thenReturn(true);

        boolean deleted = topicService.delete(TOPIC1_ID);

        assertTrue(deleted);
        verify(topicRepository).delete(TOPIC1_ID);
        verify(commentRepository).deleteByTopicId(TOPIC1_ID);
        verify(topicRepository).delete(TOPIC1_ID);
    }

    @Test
    void deleteTopicNotFoundException() {
        when(topicRepository.get(TOPIC1_ID)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> topicService.delete(TOPIC1_ID));
    }
}
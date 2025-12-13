package com.forum.forum.service;

import com.forum.forum.ForumTestData;
import com.forum.forum.UserTestData;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.CrudForumTopicRepository;
import com.forum.forum.util.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static com.forum.forum.ForumTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ForumTopicServiceTest {
    private CrudForumTopicRepository topicRepository;
    private ForumTopicService topicService;

    @BeforeEach
    void setUp() {
        topicRepository = mock(CrudForumTopicRepository.class);
        topicService = new ForumTopicService(topicRepository);
    }

    @Test
    void createTopicSuccess() {
        User author = TOPIC1.getAuthor();
        ForumTopic newTopic = ForumTestData.getNewTopic(author);

        when(topicRepository.save(any(ForumTopic.class)))
                .thenReturn(newTopic);

        ForumTopic savedTopic = topicService.createTopic(
                newTopic.getTitle(),
                newTopic.getContent(),
                UserTestData.USER);

        assertNotNull(savedTopic);
        assertEquals(newTopic.getTitle(), savedTopic.getTitle());
        assertEquals(author, savedTopic.getAuthor());


        ArgumentCaptor<ForumTopic> captor = ArgumentCaptor.forClass(ForumTopic.class);
        verify(topicRepository).save(captor.capture());
        assertEquals(savedTopic.getTitle(), captor.getValue().getTitle());
    }

    @Test
    void getAllTopicSuccess() {
        when(topicRepository.findAll()).thenReturn(ALL_TOPIC);

        List<ForumTopic> topics = topicService.getAll();

        assertEquals(ALL_TOPIC.size(), topics.size());
        assertTrue(topics.contains(TOPIC1));
        assertTrue(topics.contains(TOPIC2));
    }

    @Test
    void getTopicSuccess() {
        when(topicRepository.findById(TOPIC1_ID))
                .thenReturn(Optional.of(TOPIC1));

        ForumTopic topic = topicService.get(TOPIC1_ID);
        assertNotNull(topic);
        assertEquals(TOPIC1.getId(), topic.getId());
    }

    @Test
    void getTopicNotFound() {
        when(topicRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> topicService.get(TOPIC1_ID));
    }
}
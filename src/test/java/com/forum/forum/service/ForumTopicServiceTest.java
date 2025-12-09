package com.forum.forum.service;

import com.forum.forum.ForumTestData;
import com.forum.forum.UserTestData;
import com.forum.forum.model.ForumTopic;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.CrudForumTopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

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
    void createTopic() {
        User author = ForumTestData.TOPIC1.getAuthor();
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
    void getAll() {
        when(topicRepository.findAll()).thenReturn(ForumTestData.getAllTopics());

        List<ForumTopic> topics = topicService.getAll();

        assertEquals(2, topics.size());
        assertTrue(topics.contains(ForumTestData.TOPIC1));
        assertTrue(topics.contains(ForumTestData.TOPIC2));
    }

    @Test
    void get() {
        when(topicRepository.findById(ForumTestData.TOPIC1.getId()))
                .thenReturn(Optional.of(ForumTestData.TOPIC1));

        ForumTopic topic = topicService.get(ForumTestData.TOPIC1.getId());
        assertNotNull(topic);
        assertEquals(ForumTestData.TOPIC1.getId(), topic.getId());
    }
}
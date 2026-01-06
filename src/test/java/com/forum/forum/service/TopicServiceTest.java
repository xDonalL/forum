package com.forum.forum.service;

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

import static com.forum.forum.TopicTestData.*;
import static com.forum.forum.UserTestData.USER;
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
    void createSuccess() {
        User author = TOPIC1.getAuthor();
        Topic newTopic = getNewTopic(author);

        when(topicRepository.save(any(Topic.class)))
                .thenReturn(newTopic);

        Topic savedTopic = topicService.create(
                newTopic.getTitle(),
                newTopic.getContent(),
                USER);

        assertNotNull(savedTopic);
        assertEquals(newTopic.getTitle(), savedTopic.getTitle());
        assertEquals(author, savedTopic.getAuthor());


        ArgumentCaptor<Topic> captor = ArgumentCaptor.forClass(Topic.class);
        verify(topicRepository).save(captor.capture());
        assertEquals(savedTopic.getTitle(), captor.getValue().getTitle());
    }

//    @Test
//    void getAllTopicSuccess() {
//        when(topicRepository.getAll()).thenReturn(ALL_TOPIC);
//
//        List<Topic> topics = topicService.getAllSorted(null);
//
//        assertEquals(ALL_TOPIC.size(), topics.size());
//        assertTrue(topics.contains(TOPIC1));
//        assertTrue(topics.contains(TOPIC2));
//    }

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

//    @Test
//    void searchTopicSuccess() {
//        String topicTitle = TOPIC1.getTitle();
//        when(topicRepository.getTopicsByTopicName(topicTitle)).thenReturn((List.of(TOPIC1)));
//
//        List<Topic> topics = topicService.search(topicTitle);
//
//        assertEquals(TOPIC1.getId(), topics.getFirst().getId());
//        verify(topicRepository).getTopicsByTopicName(topicTitle);
//    }
//
//    @Test
//    void searchTopicNotFoundException() {
//        String topicTitle = TOPIC1.getTitle();
//        when(topicRepository.getTopicsByTopicName(topicTitle)).thenReturn(null);
//
//        assertThrows(NotFoundException.class,
//                () -> topicService.search(topicTitle));
//    }

    @Test
    void addLikeSuccess() {
        Integer topicId = TOPIC1.getId();
        when(topicRepository.get(topicId)).thenReturn(TOPIC1);

        assertTrue(topicService.addLike(topicId, USER));
    }

    @Test
    void addLikeTopicNotFoundException() {
        Integer topicId = TOPIC1.getId();
        when(topicRepository.get(topicId)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> topicService.addLike(topicId, USER));
    }

    @Test
    void deleteLikeSuccess() {
        Integer topicId = TOPIC1.getId();
        when(topicRepository.get(topicId)).thenReturn(TOPIC1);
        topicService.addLike(topicId, USER);

        assertTrue(topicService.deleteLike(topicId, USER));
    }

    @Test
    void deleteLikeTopicNotFoundException() {
        Integer topicId = TOPIC1.getId();
        when(topicRepository.get(topicId)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> topicService.deleteLike(topicId, USER));
    }
}
package com.forum.forum.service;

import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.model.Topic;
import com.forum.forum.model.TopicSort;
import com.forum.forum.model.User;
import com.forum.forum.repository.forum.TopicCommentRepository;
import com.forum.forum.repository.forum.TopicRepository;
import com.forum.forum.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.forum.forum.PageTestData.*;
import static com.forum.forum.TopicTestData.*;
import static com.forum.forum.UserTestData.USER;
import static com.forum.forum.model.TopicSort.DATE_DESC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicCommentRepository commentRepository;

    @InjectMocks
    private TopicService topicService;

    @Test
    void createSuccess() {
        User author = TOPIC.getAuthor();
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

    @Test
    void getAllTopicSuccess() {
        when(topicRepository.getAllTopics(PAGE_DATE_DESC)).thenReturn(ALL_TOPIC_PAGE);

        Page<TopicPagesDto> verifyTopics = topicService.getAllSorted(PAGE_NUMBER, PAGE_SIZE, DATE_DESC);

        assertEquals(ALL_TOPIC_PAGE.getTotalElements(), verifyTopics.getTotalElements());
        assertTrue(verifyTopics.getContent().contains(PAGES_TOPIC_DTO1));
        assertTrue(verifyTopics.getContent().contains(PAGES_TOPIC_DTO2));
    }

    @Test
    void getTopicSuccess() {
        when(topicRepository.get(TOPIC1_ID)).thenReturn(TOPIC);

        Topic topic = topicService.get(TOPIC1_ID);
        assertNotNull(topic);
        assertEquals(TOPIC.getId(), topic.getId());
    }

    @Test
    void getTopicNotFound() {
        when(topicRepository.get(anyInt())).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> topicService.get(TOPIC1_ID));
    }

    @Test
    void deleteTopicSuccess() {
        when(topicRepository.get(TOPIC1_ID)).thenReturn(TOPIC);
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

    @Test
    void searchTopicSuccess() {
        String topicTitle = PAGES_TOPIC_DTO1.title();

        when(topicRepository.getTopicsByTitle(topicTitle, PAGE))
                .thenReturn(new PageImpl<>(List.of(PAGES_TOPIC_DTO1)));

        Page<TopicPagesDto> verifyTopics = topicService.search(PAGE_NUMBER, PAGE_SIZE, topicTitle);

        assertEquals(PAGES_TOPIC_DTO1.id(), verifyTopics.get().findFirst().get().id());
        verify(topicRepository).getTopicsByTitle(topicTitle, PAGE);
    }

    @Test
    void searchTopicNotFoundException() {
        String topicTitle = PAGES_TOPIC_DTO1.title();

        when(topicRepository.getTopicsByTitle(topicTitle, PAGE))
                .thenReturn(null);
        assertThrows(NotFoundException.class,
                () -> topicService.search(PAGE_NUMBER, PAGE_SIZE, topicTitle));
    }
}
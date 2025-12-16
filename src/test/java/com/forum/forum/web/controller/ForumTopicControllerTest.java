package com.forum.forum.web.controller;

import com.forum.forum.model.ForumTopic;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.service.ForumTopicService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.forum.forum.ForumTestData.*;
import static com.forum.forum.UserTestData.USER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForumTopicController.class)
@Import(SecurityConfig.class)
class ForumTopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumTopicService topicService;

    @MockBean
    private UserService userService;

    @Test
    void listTopics() throws Exception {
        List<ForumTopic> topics = List.of(TOPIC1, TOPIC2);
        when(topicService.getAll()).thenReturn(topics);

        mockMvc.perform(get("/forum"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topics"))
                .andExpect(model().attribute("topics", topics))
                .andExpect(view().name("topic-list"));
    }

    @Test
    void topicPage() throws Exception {
        when(topicService.get(TOPIC1_ID)).thenReturn(TOPIC1);

        mockMvc.perform(get("/forum/" + TOPIC1_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topic"))
                .andExpect(model().attributeExists("content"))
                .andExpect(view().name("topic-page"));
    }

    @Test
    void createTopic() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post("/forum/add/topic")
                        .param("title", TOPIC1.getTitle())
                        .param("content", TOPIC1.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forum"));

        verify(topicService).createTopic(TOPIC1.getTitle(), TOPIC1.getContent(), USER);
    }
}
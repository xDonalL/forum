package com.forum.forum.web.controller;

import com.forum.forum.TestUrls;
import com.forum.forum.dto.TopicPageDto;
import com.forum.forum.dto.TopicPagesDto;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.TopicService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;

import static com.forum.forum.TopicTestData.*;
import static com.forum.forum.UserTestData.USER;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TopicController.class)
@Import(SecurityConfig.class)
class TopicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;

    @MockBean
    private UserService userService;

    @MockBean
    private AdminLogService adminLogService;

    @Test
    void getTopics_thenOkAndModel() throws Exception {
        Page<TopicPagesDto> page = Page.empty();
        when(topicService.getAllSorted(0, 10, null)).thenReturn(page);

        mockMvc.perform(get(TestUrls.TOPIC_LIST))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topics"))
                .andExpect(model().attribute("topics", page))
                .andExpect(view().name("topic/list"));
    }

    @Test
    void getTopicPage_thenOkAndModel() throws Exception {
        TopicPageDto dto = new TopicPageDto(TOPIC_DTO, Page.empty());
        when(topicService.getDto(0, 10, TOPIC1_ID)).thenReturn(dto);

        mockMvc.perform(get(TestUrls.topicView(TOPIC1_ID)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topic"))
                .andExpect(model().attributeExists("comments"))
                .andExpect(view().name("topic/view"));
    }

    @Test
    void getAddTopic_thenOk() throws Exception {
        mockMvc.perform(get(TestUrls.TOPIC_ADD))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topicTo"))
                .andExpect(view().name("topic/form"));
    }

    @Test
    void postAddTopic_whenValid_thenRedirect() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.TOPIC_ADD)
                        .with(csrf())
                        .param("title", "new title")
                        .param("content", "new content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.TOPIC_LIST));

        verify(topicService).create("new title", "new content", USER);
    }

    @Test
    void postAddTopic_whenInvalid_thenReturnForm() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.TOPIC_ADD)
                        .with(csrf())
                        .param("title", "")
                        .param("content", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("topic/form"));

        verify(topicService, never()).create(any(), any(), any());
    }

    @Test
    void getEditTopic_thenOk() throws Exception {
        when(topicService.get(TOPIC1_ID)).thenReturn(TOPIC);

        mockMvc.perform(get(TestUrls.topicEdit(TOPIC1_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("topic/form"))
                .andExpect(model().attributeExists("topicTo"));
    }

    @Test
    void postEditTopic_whenValid_thenRedirect() throws Exception {
        mockMvc.perform(post(TestUrls.topicEdit(TOPIC1_ID))
                        .with(csrf())
                        .param("title", "new title")
                        .param("content", "new content"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(topicService).update(TOPIC1_ID, "new title", "new content");
    }

    @Test
    void postEditTopic_whenInvalid_thenReturnEdit() throws Exception {
        mockMvc.perform(post(TestUrls.topicEdit(TOPIC1_ID))
                        .with(csrf())
                        .param("title", "")
                        .param("content", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("topic/form"));

        verify(topicService, never()).update(any(), any(), any());
    }

    @Test
    void postDeleteTopic_thenRedirect() throws Exception {
        when(topicService.get(TOPIC1_ID)).thenReturn(TOPIC);
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.topicDelete(TOPIC1_ID))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.TOPIC_LIST));

        verify(topicService).delete(TOPIC1_ID);
        verify(adminLogService).logAction(any(), any(), any(), any());
    }

    @Test
    void postLikeTopic_thenRedirect() throws Exception {
        mockMvc.perform(post(TestUrls.TOPIC_LIKE)
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(topicService).toggleLike(TOPIC1_ID);
    }
}
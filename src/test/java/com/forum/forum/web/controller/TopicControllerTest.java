package com.forum.forum.web.controller;

import com.forum.forum.TestUrls;
import com.forum.forum.model.Topic;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.security.TopicSecurity;
import com.forum.forum.service.TopicService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.forum.forum.AuthTestData.getAuthToken;
import static com.forum.forum.TopicTestData.*;
import static com.forum.forum.UserTestData.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
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

    @MockBean(name = "topicSecurity")
    private TopicSecurity topicSecurity;

//    @Test
//    void getListTopics_whenNotAuth_thenIsOk() throws Exception {
//        List<Topic> topics = List.of(TOPIC1, TOPIC2);
//        when(topicService.getAllSorted(null)).thenReturn(topics);
//
//        mockMvc.perform(get(TestUrls.TOPIC_LIST))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("topics"))
//                .andExpect(model().attribute("topics", topics))
//                .andExpect(view().name("topic/list"));
//    }

    @Test
    void getTopicPage_whenNotAuth_thenIsOk() throws Exception {
        when(topicService.get(TOPIC1_ID)).thenReturn(TOPIC1);

        mockMvc.perform(get(TestUrls.topicView(TOPIC1_ID)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topic"))
                .andExpect(model().attributeExists("content"))
                .andExpect(view().name("topic/view"));
    }

    @Test
    void getAddTopic_whenUser_thenRedirectAndSuccess() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.TOPIC_ADD)
                        .with(authentication(getAuthToken(USER)))
                        .param("title", TOPIC1.getTitle())
                        .param("content", TOPIC1.getContent())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.TOPIC_LIST));

        verify(topicService).create(TOPIC1.getTitle(), TOPIC1.getContent(), USER);
    }

    @Test
    void postAddTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post(TestUrls.TOPIC_ADD)
                        .with(csrf())
                        .param("title", TOPIC1.getTitle())
                        .param("content", TOPIC1.getContent()))
                .andExpect(redirectedUrlPattern(TestUrls.LOGIN_VIEW));
    }

    @Test
    void getEditTopic_whenIsOwner_thenIsOk() throws Exception {
        when(topicSecurity.isOwner(USER_ID)).thenReturn(true);
        when(topicService.get(TOPIC1_ID)).thenReturn(TOPIC1);

        mockMvc.perform(get(TestUrls.topicEdit(TOPIC1_ID)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topic"))
                .andExpect(model().attribute("topic", TOPIC1))
                .andExpect(view().name("topic/edit"));

        verify(topicService).get(TOPIC1_ID);
    }

    @Test
    void postEditTopic_whenIsOwner_thenRedirectAndSuccess() throws Exception {
        when(topicSecurity.isOwner(USER_ID)).thenReturn(true);

        mockMvc.perform(post(TestUrls.topicEdit(TOPIC1_ID))
                        .param("title", TOPIC1.getTitle())
                        .param("content", TOPIC1.getContent())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(topicService).update(TOPIC1_ID, TOPIC1.getTitle(), TOPIC1.getContent());
    }

    @Test
    void postEditTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post(TestUrls.topicEdit(TOPIC1_ID))
                        .param("title", TOPIC1.getTitle())
                        .param("content", TOPIC1.getContent())
                        .with(csrf()))
                .andExpect(redirectedUrlPattern(TestUrls.LOGIN_VIEW));
    }

    @Test
    void postEditTopic_whenNotOwner_then() throws Exception {
        when(topicSecurity.isOwner(TOPIC1_ID)).thenReturn(false);

        mockMvc.perform(post(TestUrls.topicEdit(TOPIC1_ID))
                        .with(authentication(getAuthToken(USER)))
                        .param("content", "update"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postDeleteTopic_whenAdmin_thenRedirectAndSuccess() throws Exception {
        mockMvc.perform(post(TestUrls.topicDelete(TOPIC1_ID))
                        .with(authentication(getAuthToken(ADMIN)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.TOPIC_LIST));

        verify(topicService).delete(TOPIC1_ID);
    }

    @Test
    void postDeleteTopic_whenModer_thenRedirectAndSuccess() throws Exception {
        mockMvc.perform(post(TestUrls.topicDelete(TOPIC1_ID))
                        .with(authentication(getAuthToken(MODER)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.TOPIC_LIST));

        verify(topicService).delete(TOPIC1_ID);
    }

    @Test
    void postDeleteTopic_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(post(TestUrls.topicDelete(TOPIC1_ID))
                        .with(authentication(getAuthToken(USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void postDeleteTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post(TestUrls.topicDelete(TOPIC1_ID))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(TestUrls.LOGIN_VIEW));
    }

    @Test
    void postAddLikeTopic_whenUser_thenRedirectAndSuccess() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.TOPIC_LIKE_ADD)
                        .with(authentication(getAuthToken(USER)))
                        .param("id", String.valueOf(TOPIC1.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(topicService).addLike(TOPIC1.getId(), USER);
    }

    @Test
    void postAddLikeTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post(TestUrls.TOPIC_LIKE_ADD)
                        .param("id", String.valueOf(TOPIC1.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(TestUrls.LOGIN_VIEW));
    }

    @Test
    void postDeleteLikeTopic_whenUser_thenRedirectAndSuccess() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.TOPIC_LIKE_DELETE)
                        .with(authentication(getAuthToken(USER)))
                        .param("id", String.valueOf(TOPIC1.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(topicService).deleteLike(TOPIC1.getId(), USER);
    }

    @Test
    void postDeleteLikeTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post(TestUrls.TOPIC_LIKE_DELETE)
                        .param("id", String.valueOf(TOPIC1.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern(TestUrls.LOGIN_VIEW));
    }
}
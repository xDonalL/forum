package com.forum.forum.web.controller;

import com.forum.forum.model.Role;
import com.forum.forum.model.Topic;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.security.TopicSecurity;
import com.forum.forum.service.TopicService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.forum.forum.ForumTopicTestData.*;
import static com.forum.forum.UserTestData.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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

    @Test
    void getListTopics_whenNotAuth_thenIsOk() throws Exception {
        List<Topic> topics = List.of(TOPIC1, TOPIC2);
        when(topicService.getAllSorted(null)).thenReturn(topics);

        mockMvc.perform(get("/topic"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topics"))
                .andExpect(model().attribute("topics", topics))
                .andExpect(view().name("topic/list"));
    }

    @Test
    void getTopicPage_whenNotAuth_thenIsOk() throws Exception {
        when(topicService.get(TOPIC1_ID)).thenReturn(TOPIC1);

        mockMvc.perform(get("/topic/" + TOPIC1_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topic"))
                .andExpect(model().attributeExists("content"))
                .andExpect(view().name("topic/view"));
    }

    @Test
    void getAddTopic_whenUser_thenRedirectAndSuccess() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post("/topic/add")
                        .with(user(USER.getEmail()).roles(String.valueOf(Role.USER)))
                        .param("title", TOPIC1.getTitle())
                        .param("content", TOPIC1.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic"));

        verify(topicService).create(TOPIC1.getTitle(), TOPIC1.getContent(), USER);
    }

    @Test
    void postAddTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/add")
                        .with(csrf())
                        .param("title", TOPIC1.getTitle())
                        .param("content", TOPIC1.getContent()))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void getEditTopic_whenIsOwner_thenIsOk() throws Exception {
        when(topicSecurity.isOwner(USER_ID)).thenReturn(true);
        when(topicService.get(TOPIC1_ID)).thenReturn(TOPIC1);

        mockMvc.perform(get("/topic/edit/" + TOPIC1_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("topic"))
                .andExpect(model().attribute("topic", TOPIC1))
                .andExpect(view().name("topic/edit"));

        verify(topicService).get(TOPIC1_ID);
    }

    @Test
    void postEditTopic_whenIsOwner_thenRedirectAndSuccess() throws Exception {
        when(topicSecurity.isOwner(USER_ID)).thenReturn(true);

        mockMvc.perform(post("/topic/edit/" + TOPIC1_ID)
                        .param("content", TOPIC1.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1_ID));

        verify(topicService).update(TOPIC1_ID, TOPIC1.getContent());
    }

    @Test
    void postEditTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/edit/" + TOPIC1_ID)
                        .param("content", TOPIC1.getContent()))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void postEditTopic_whenNotOwner_then() throws Exception {
        when(topicSecurity.isOwner(TOPIC1_ID)).thenReturn(false);

        mockMvc.perform(post("/topic/edit/" + TOPIC1_ID)
                        .with(user(USER.getEmail()).roles(String.valueOf(Role.USER)))
                        .param("content", "update"))
                .andExpect(status().isForbidden());
    }

    @Test
    void postDeleteTopic_whenAdmin_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authAdmin = new AuthorizedUser(ADMIN);

        mockMvc.perform(post("/topic/delete/" + TOPIC1_ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authAdmin, null, authAdmin.getAuthorities()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic"));

        verify(topicService).delete(TOPIC1_ID);
    }

    @Test
    void postDeleteTopic_whenModer_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authModer = new AuthorizedUser(MODER);

        mockMvc.perform(post("/topic/delete/" + TOPIC1_ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authModer, null, authModer.getAuthorities()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic"));

        verify(topicService).delete(TOPIC1_ID);
    }

    @Test
    void postDeleteTopic_whenUser_thenForbidden() throws Exception {
        AuthorizedUser authUser = new AuthorizedUser(USER);

        mockMvc.perform(post("/topic/delete/" + TOPIC1_ID)
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authUser, null, authUser.getAuthorities()))))
                .andExpect(status().isForbidden());
    }

    @Test
    void postDeleteTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/delete/" + TOPIC1_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void postAddLikeTopic_whenUser_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authUser = new AuthorizedUser(USER);

        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post("/topic/like/add")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        authUser, null, authUser.getAuthorities())))
                        .param("id", String.valueOf(TOPIC1.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1.getId()));

        verify(topicService).addLike(TOPIC1.getId(), USER);
    }

    @Test
    void postAddLikeTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/like/add")
                        .param("id", String.valueOf(TOPIC1.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void postDeleteLikeTopic_whenUser_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authUser = new AuthorizedUser(USER);

        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post("/topic/like/delete")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        authUser, null, authUser.getAuthorities())))
                        .param("id", String.valueOf(TOPIC1.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1.getId()));

        verify(topicService).deleteLike(TOPIC1.getId(), USER);
    }

    @Test
    void postDeleteLikeTopic_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/like/delete")
                        .param("id", String.valueOf(TOPIC1.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
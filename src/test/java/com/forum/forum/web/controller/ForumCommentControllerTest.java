package com.forum.forum.web.controller;

import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.service.ForumCommentService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static com.forum.forum.ForumCommentTestData.COMMENT1_ID;
import static com.forum.forum.ForumTopicTestData.TOPIC1;
import static com.forum.forum.ForumTopicTestData.TOPIC1_ID;
import static com.forum.forum.UserTestData.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForumCommentController.class)
@Import(SecurityConfig.class)
class ForumCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumCommentService commentService;

    @MockBean
    private UserService userService;

    @Test
    void addComment() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        AuthorizedUser authorizedUser = new AuthorizedUser(USER);

        mockMvc.perform(post("/forum/topic/comment/add")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedUser, null, authorizedUser.getAuthorities())))
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .param("comment", TOPIC1.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forum/" + TOPIC1_ID));

        verify(commentService).addComment(TOPIC1_ID, USER, TOPIC1.getContent());
    }

    @Test
    void deleteCommentByAdmin() throws Exception {
        AuthorizedUser authorizedAdmin = new AuthorizedUser(ADMIN);

        mockMvc.perform(post("/forum/topic/comment/delete/" + COMMENT1_ID)
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedAdmin, null, authorizedAdmin.getAuthorities()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forum/" + TOPIC1_ID));
    }

    @Test
    void deleteCommentByModer() throws Exception {
        AuthorizedUser authorizedModer = new AuthorizedUser(MODER);

        mockMvc.perform(post("/forum/topic/comment/delete/" + COMMENT1_ID)
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedModer, null, authorizedModer.getAuthorities()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forum/" + TOPIC1_ID));
    }

    @Test
    void deleteCommentForbiddenForUser() throws Exception {
        AuthorizedUser authorizedUser = new AuthorizedUser(USER);

        mockMvc.perform(post("/forum/topic/comment/delete/" + COMMENT1_ID)
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .with(user(USER.getEmail()).roles("USER")))
                .andExpect(status().isForbidden());
    }
}
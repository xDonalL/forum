package com.forum.forum.web.controller;

import com.forum.forum.model.Role;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.security.TopicCommentSecurity;
import com.forum.forum.service.TopicCommentService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static com.forum.forum.TopicCommentTestData.COMMENT1;
import static com.forum.forum.TopicCommentTestData.COMMENT1_ID;
import static com.forum.forum.TopicTestData.TOPIC1;
import static com.forum.forum.TopicTestData.TOPIC1_ID;
import static com.forum.forum.UserTestData.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TopicCommentController.class)
@Import(SecurityConfig.class)
class TopicCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicCommentService commentService;

    @MockBean
    private UserService userService;

    @MockBean(name = "commentSecurity")
    private TopicCommentSecurity commentSecurity;

    @Test
    void postAddComment_whenUser_thenRedirectAndAddComment() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        AuthorizedUser authorizedUser = new AuthorizedUser(USER);

        mockMvc.perform(post("/topic/comment/add")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedUser, null, authorizedUser.getAuthorities()))
                        )
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .param("comment", TOPIC1.getContent()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1_ID));

        verify(commentService).add(TOPIC1_ID, USER, TOPIC1.getContent());
    }

    @Test
    void postAddComment_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/comment/add")
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .param("comment", TOPIC1.getContent()))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void postDeleteComment_whenAdmin_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authorizedAdmin = new AuthorizedUser(ADMIN);

        mockMvc.perform(post("/topic/comment/delete/" + COMMENT1_ID)
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedAdmin, null, authorizedAdmin.getAuthorities()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1_ID));

        verify(commentService).delete(COMMENT1_ID);
    }

    @Test
    void postDeleteComment_whenModer_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authorizedModer = new AuthorizedUser(MODER);

        mockMvc.perform(post("/topic/comment/delete/" + COMMENT1_ID)
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedModer, null, authorizedModer.getAuthorities()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1_ID));

        verify(commentService).delete(COMMENT1_ID);
    }

    @Test
    void postDeleteComment_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(post("/topic/comment/delete/" + COMMENT1_ID)
                        .with(csrf())
                        .with(user(USER.getLogin()).roles("USER"))
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    void postDeleteComment_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/comment/delete/" + COMMENT1_ID)
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void postEditComment_whenIsOwner_thenRedirectAndSuccess() throws Exception {
        String updateComment = "updated comment";

        when(commentSecurity.isOwner(COMMENT1_ID)).thenReturn(true);

        mockMvc.perform(post("/topic/comment/edit/" + COMMENT1_ID)
                        .param("text", updateComment)
                        .param("topicId", TOPIC1_ID.toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1_ID));

        verify(commentService).update(COMMENT1_ID, updateComment);
    }

    @Test
    void postEditComment_whenNotOwner_thenForbidden() throws Exception {
        when(commentSecurity.isOwner(COMMENT1_ID)).thenReturn(false);

        mockMvc.perform(post("/topic/comment/edit/" + COMMENT1_ID)
                        .with(user(USER.getEmail()).roles(String.valueOf(Role.USER)))
                        .param("text", "updated")
                        .param("topicId", TOPIC1_ID.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    void postEditComment_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/comment/edit/" + COMMENT1_ID)
                        .param("text", "updated")
                        .param("topicId", TOPIC1_ID.toString())
                        .with(csrf()))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void getEditComment_whenIsOwner_thenIsOk() throws Exception {
        when(commentSecurity.isOwner(COMMENT1_ID)).thenReturn(true);
        when(commentService.get(COMMENT1_ID)).thenReturn(COMMENT1);

        mockMvc.perform(get("/topic/comment/edit/" + COMMENT1_ID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("comment"))
                .andExpect(model().attribute("comment", COMMENT1))
                .andExpect(view().name("comment-edit"));

        verify(commentService).get(COMMENT1_ID);
    }

    @Test
    void getEditComment_whenNotOwner_thenForbidden() throws Exception {
        when(commentSecurity.isOwner(COMMENT1_ID)).thenReturn(false);
        when(commentService.get(COMMENT1_ID)).thenReturn(COMMENT1);

        mockMvc.perform(get("/topic/comment/edit/" + COMMENT1_ID)
                        .with(user(USER.getEmail()).roles(String.valueOf(Role.USER))))
                .andExpect(status().isForbidden());

    }

    @Test
    void postAddLikeComment_whenUser_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authorizedUser = new AuthorizedUser(USER);
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post("/topic/comment/like/add")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedUser, null, authorizedUser.getAuthorities())))
                        .with(csrf())
                        .param("id", String.valueOf(COMMENT1_ID))
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1_ID));

        verify(commentService).addLike(COMMENT1_ID, USER);

    }

    @Test
    void postAddLikeComment_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/comment/like/add")
                        .with(csrf())
                        .param("id", String.valueOf(COMMENT1_ID))
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void postDeleteLikeComment_whenUser_thenRedirectAndSuccess() throws Exception {
        AuthorizedUser authorizedUser = new AuthorizedUser(USER);
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post("/topic/comment/like/delete")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authorizedUser, null, authorizedUser.getAuthorities())))
                        .with(csrf())
                        .param("id", String.valueOf(COMMENT1_ID))
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/topic/" + TOPIC1_ID));

        verify(commentService).deleteLike(COMMENT1_ID, USER);
    }

    @Test
    void postDeleteLikeComment_whenNotAuth_thenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/topic/comment/like/delete")
                        .with(csrf())
                        .param("id", String.valueOf(COMMENT1_ID))
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .with(csrf()))
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
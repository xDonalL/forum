package com.forum.forum.web.controller;

import com.forum.forum.TestUrls;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.TopicCommentService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import static com.forum.forum.AuthTestData.getAuthToken;
import static com.forum.forum.TopicCommentTestData.COMMENT1;
import static com.forum.forum.TopicCommentTestData.COMMENT1_ID;
import static com.forum.forum.TopicTestData.TOPIC1_ID;
import static com.forum.forum.UserTestData.*;
import static com.forum.forum.model.ActionLog.DELETE_TOPIC;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TopicCommentController.class)
@Import(SecurityConfig.class)
class TopicCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    SpringSecurityDialect springSecurityDialect;

    @MockBean
    private TopicCommentService commentService;

    @MockBean
    private UserService userService;

    @MockBean
    private AdminLogService adminLogService;

    @Test
    void postAddComment_whenValid_thenRedirectAndAdd() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.COMMENT_ADD)
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .param("comment", COMMENT1.getComment()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(commentService).add(TOPIC1_ID, USER, COMMENT1.getComment());
    }

    @Test
    void postAddComment_whenInvalid_thenRedirectWithErrors() throws Exception {
        when(userService.getCurrentUser()).thenReturn(USER);

        mockMvc.perform(post(TestUrls.COMMENT_ADD)
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .param("comment", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(commentService, never()).add(any(), any(), any());
    }

    @Test
    void getEditCommentPage_thenOkAndModel() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TopicCommentController(
                        commentService, userService, adminLogService))
                .build();

        when(commentService.get(COMMENT1_ID)).thenReturn(COMMENT1);

        mockMvc.perform(get(TestUrls.commentEdit(COMMENT1_ID)))
                .andExpect(model().attributeExists("commentTo"))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-edit"));
    }

    @Test
    void postEditComment_whenValid_thenRedirect() throws Exception {
        mockMvc.perform(post(TestUrls.commentEdit(COMMENT1_ID))
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .param("comment", "updated comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(commentService).update(COMMENT1_ID, "updated comment");
    }

    @Test
    void postEditComment_whenInvalid_thenReturnEditView() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TopicCommentController(
                        commentService, userService, adminLogService))
                .build();

        mockMvc.perform(post(TestUrls.commentEdit(COMMENT1_ID))
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID))
                        .param("comment", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("comment-edit"));

        verify(commentService, never()).update(any(), any());
    }

    @Test
    void postDeleteComment_thenRedirectAndDeleteAndLog() throws Exception {
        when(commentService.get(COMMENT1_ID)).thenReturn(COMMENT1);
        when(userService.getCurrentUser()).thenReturn(ADMIN);

        mockMvc.perform(post(TestUrls.commentDelete(COMMENT1_ID))
                        .with(csrf())
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(commentService).delete(COMMENT1_ID);
        verify(adminLogService).logAction(
                ADMIN.getLogin(),
                DELETE_TOPIC,
                COMMENT1.getAuthor().getLogin(),
                COMMENT1_ID);
    }

    @Test
    void postToggleLike_thenRedirect() throws Exception {
        mockMvc.perform(post(TestUrls.COMMENT_LIKE)
                        .with(csrf())
                        .param("commentId", String.valueOf(COMMENT1_ID))
                        .param("topicId", String.valueOf(TOPIC1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TestUrls.topicView(TOPIC1_ID)));

        verify(commentService).toggleLike(COMMENT1_ID);
    }
}
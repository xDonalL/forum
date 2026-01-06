package com.forum.forum.web.controller;

import com.forum.forum.TestUrls;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.service.AdminLogService;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static com.forum.forum.AdminLogTestData.TEST_LOG;
import static com.forum.forum.AuthTestData.getAuthToken;
import static com.forum.forum.UserTestData.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AdminLogService logService;

//    @Test
//    void getShowUsersPanel_whenAdmin_thenIsOk() throws Exception {
//        when(userService.filterUsers(null)).thenReturn(ALL_USERS);
//
//        mockMvc.perform(get(TestUrls.ADMIN_PANEL)
//                        .with(authentication(getAuthToken(ADMIN))))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("users"))
//                .andExpect(model().attribute("users", ALL_USERS))
//                .andExpect(view().name("admin/panel"));
//
//        verify(userService).filterUsers(null);
//    }

    @Test
    void getShowUsersPanel_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(get(TestUrls.ADMIN_PANEL)
                        .with(authentication(getAuthToken(USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getShowUsersPanel_whenNotAuthorized_thenRedirectLogin() throws Exception {
        mockMvc.perform(get(TestUrls.ADMIN_PANEL))
                .andExpect(redirectedUrlPattern(TestUrls.LOGIN_VIEW));
    }

//    @Test
//    void postBanUser_whenAdmin_thenSuccess() throws Exception {
//        mockMvc.perform(post(TestUrls.adminBan(USER_ID))
//                        .with(authentication(getAuthToken(ADMIN)))
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(TestUrls.ADMIN_PANEL));
//
//        verify(userService).banUser(USER_ID);
//    }

    @Test
    void postBanUser_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(post(TestUrls.adminBan(USER_ID))
                        .with(authentication(getAuthToken(USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void postBanUser_whenModer_thenForbidden() throws Exception {
        mockMvc.perform(post(TestUrls.adminBan(USER_ID))
                        .with(authentication(getAuthToken(MODER))))
                .andExpect(status().isForbidden());
    }

//    @Test
//    void postUnbanUser_whenAdmin_thenSuccess() throws Exception {
//        mockMvc.perform(post(TestUrls.adminUnban(USER_ID))
//                        .with(authentication(getAuthToken(ADMIN)))
//                        .with(csrf()))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(TestUrls.ADMIN_PANEL));
//
//        verify(userService).unbanUser(USER_ID);
//    }

    @Test
    void postUnbanUser_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(post(TestUrls.adminUnban(USER_ID))
                        .with(authentication(getAuthToken(USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void postUnbanUser_whenModer_thenForbidden() throws Exception {
        mockMvc.perform(post(TestUrls.adminUnban(USER_ID))
                        .with(authentication(getAuthToken(MODER))))
                .andExpect(status().isForbidden());
    }

//    @Test
//    void getShowLogs_whenAdmin_thenIsOk() throws Exception {
//        when(logService.getAll()).thenReturn(TEST_LOG);
//
//        mockMvc.perform(get("/admin/log")
//                        .with(authentication(getAuthToken(ADMIN))))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("logs"))
//                .andExpect(model().attribute("logs", TEST_LOG))
//                .andExpect(view().name("admin/log"));
//    }

    @Test
    void getShowLogs_whenModer_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/log")
                        .with(authentication(getAuthToken(USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getShowLogs_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/log")
                        .with(authentication(getAuthToken(USER))))
                .andExpect(status().isForbidden());
    }
}
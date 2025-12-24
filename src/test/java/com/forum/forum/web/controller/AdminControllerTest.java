package com.forum.forum.web.controller;

import com.forum.forum.model.Role;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static com.forum.forum.UserTestData.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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

    @Test
    void getShowUsersPanel_whenAdmin_thenIsOk() throws Exception {
        when(userService.filterUsers(null)).thenReturn(ALL_USERS);

        AuthorizedUser authAdmin = new AuthorizedUser(ADMIN);

        mockMvc.perform(get("/admin/panel")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authAdmin, null, authAdmin.getAuthorities()))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", ALL_USERS))
                .andExpect(view().name("admin/panel"));

        verify(userService).filterUsers(null);
    }

    @Test
    void getShowUsersPanel_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/panel")
                        .with(user(USER.getEmail()).roles(String.valueOf(Role.USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getShowUsersPanel_whenNotAuthorized_thenRedirectLogin() throws Exception {
        mockMvc.perform(get("/admin/panel"))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void postBanUser_whenAdmin_thenSuccess() throws Exception {
        mockMvc.perform(post("/admin/ban/" + USER_ID)
                        .with(user(ADMIN.getEmail()).roles(String.valueOf(Role.ADMIN)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel"));

        verify(userService).banUser(USER_ID);
    }

    @Test
    void postBanUser_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(post("/admin/ban/" + USER_ID)
                        .with(user(USER.getEmail()).roles(String.valueOf(Role.USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void postBanUser_whenModer_thenForbidden() throws Exception {
        mockMvc.perform(post("/admin/ban/" + USER_ID)
                        .with(user(MODER.getEmail()).roles(String.valueOf(Role.MODERATOR))))
                .andExpect(status().isForbidden());
    }

    @Test
    void postUnbanUser_whenAdmin_thenSuccess() throws Exception {
        mockMvc.perform(post("/admin/unban/" + USER_ID)
                        .with(user(ADMIN.getEmail()).roles(String.valueOf(Role.ADMIN)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/panel"));

        verify(userService).unbanUser(USER_ID);
    }

    @Test
    void postUnbanUser_whenUser_thenForbidden() throws Exception {
        mockMvc.perform(post("/admin/unban/" + USER_ID)
                        .with(user(USER.getEmail()).roles(String.valueOf(Role.USER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void postUnbanUser_whenModer_thenForbidden() throws Exception {
        mockMvc.perform(post("/admin/unban/" + USER_ID)
                        .with(user(MODER.getEmail()).roles(String.valueOf(Role.MODERATOR))))
                .andExpect(status().isForbidden());
    }
}
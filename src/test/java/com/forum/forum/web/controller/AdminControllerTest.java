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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void usersPage() throws Exception {
        when(userService.getAll()).thenReturn(ALL_USERS);

        AuthorizedUser authAdmin = new AuthorizedUser(ADMIN);

        mockMvc.perform(get("/admin/panel")
                        .with(authentication(new UsernamePasswordAuthenticationToken(
                                authAdmin, null, authAdmin.getAuthorities()))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", ALL_USERS))
                .andExpect(view().name("admin/panel"));

        verify(userService).getAll();
    }

    @Test
    void usersPageForbiddenForUser() throws Exception {
        mockMvc.perform(get("/admin/panel")
                        .with(user(USER.getEmail())
                                .roles(String.valueOf(Role.USER))))
                .andExpect(status().isForbidden());
    }
}
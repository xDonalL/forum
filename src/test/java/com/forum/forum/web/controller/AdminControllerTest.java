package com.forum.forum.web.controller;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;
import com.forum.forum.security.SecurityConfig;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.forum.forum.UserTestData.ADMIN;
import static com.forum.forum.UserTestData.USER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        List<User> users = List.of(USER, ADMIN);
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/admin/users").with(user(ADMIN.getEmail())
                        .roles(String.valueOf(Role.ADMIN))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", users))
                .andExpect(view().name("users"));

        verify(userService).getAll();
    }

    @Test
    void usersPageForbiddenForUser() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(user(USER.getEmail())
                                .roles(String.valueOf(Role.USER))))
                .andExpect(status().isForbidden());
    }
}
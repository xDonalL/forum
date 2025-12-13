package com.forum.forum.web.controller;

import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static com.forum.forum.UserTestData.USER;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void profilePage() throws Exception {
        when(userService.getUserById(USER.getId())).thenReturn(USER);

        AuthorizedUser authUser = new AuthorizedUser(USER);

        mockMvc.perform(get("/profile/" + USER.getId() + "-" + USER.getLogin())
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        authUser, null, authUser.getAuthorities()))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", USER))
                .andExpect(view().name("user-profile"));

        verify(userService).getUserById(USER.getId());
    }
}
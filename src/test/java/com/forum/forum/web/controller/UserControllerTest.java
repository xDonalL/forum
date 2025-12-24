package com.forum.forum.web.controller;

import com.forum.forum.model.User;
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
import org.springframework.web.multipart.MultipartFile;

import static com.forum.forum.UserTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getProfilePage_whenNotAuth_thenIsOk() throws Exception {
        when(userService.getUserById(USER_ID)).thenReturn(USER);

        mockMvc.perform(get("/profile/" + USER_ID + "-" + USER.getLogin()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", USER))
                .andExpect(view().name("profile/view"));

        verify(userService).getUserById(USER_ID);
    }

    @Test
    void getProfileEditPage_whenUser_thenIsOk() throws Exception {
        when(userService.getByEmail(USER.getEmail())).thenReturn(USER);

        AuthorizedUser authUser = new AuthorizedUser(USER);

        mockMvc.perform(get("/profile/edit")
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        authUser, null, authUser.getAuthorities()))))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", USER))
                .andExpect(view().name("profile/edit"));

        verify(userService).getByEmail(USER.getEmail());
    }

    @Test
    void postUpdateProfile_whenUser_thenRedirectAndSuccess() throws Exception {
        when(userService.getByEmail(USER.getEmail())).thenReturn(USER);
        when(userService.update(USER, null)).thenReturn(USER);

        mockMvc.perform(multipart("/profile/edit")
                        .file(AVATAR)
                        .param("name", "newName")
                        .with(user(USER.getEmail()).roles("USER"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/" + USER.getId() + "-" + USER.getLogin()));

        verify(userService).getByEmail(USER.getEmail());
        verify(userService).update(any(User.class), any(MultipartFile.class));
    }
}
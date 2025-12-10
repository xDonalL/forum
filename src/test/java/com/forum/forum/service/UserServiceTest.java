package com.forum.forum.service;

import com.forum.forum.model.User;
import com.forum.forum.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.forum.forum.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(USER);

        User saved = userService.createUser(USER);

        assertNotNull(saved);
        assertEquals(USER.getName(), saved.getName());
        verify(userRepository).save(USER);
    }

    @Test
    void deleteUser() {
        when(userRepository.delete(USER_ID)).thenReturn(true);

        boolean deleted = userService.delete(USER_ID);

        assertTrue(deleted);
        verify(userRepository).delete(USER_ID);
    }

    @Test
    void getByEmail() {
        when(userRepository.getByEmail(getNew().getEmail())).thenReturn(getNew());

        User result = userService.getByEmail(getNew().getEmail());

        assertEquals(getNew().getEmail(), result.getEmail());
    }

    @Test
    void getUserById() {
        User user = getNew();
        user.setId(USER_ID);
        when(userRepository.get(user.getId())).thenReturn(user);

        User result = userService.getUserById(USER_ID);

        assertEquals(USER_ID, result.getId());
    }

    @Test
    void getAll() {
        List<User> list = List.of(USER, ADMIN);

        when(userRepository.getAll()).thenReturn(list);

        List<User> all = userService.getAll();

        assertEquals(list.size(), all.size());
    }
}
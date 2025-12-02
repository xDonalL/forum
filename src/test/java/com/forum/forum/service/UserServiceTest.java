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
        when(userRepository.save(any(User.class))).thenReturn(getNew());

        User saved = userService.createUser(getNew());

        assertNotNull(saved);
        assertEquals(getNew().getName(), saved.getName());
        verify(userRepository).save(getNew());
    }

    @Test
    void deleteUser() {
        when(userRepository.delete(1)).thenReturn(true);

        boolean deleted = userService.delete(1);

        assertTrue(deleted);
        verify(userRepository).delete(1);
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
        user.setId(1);
        when(userRepository.get(user.getId())).thenReturn(user);

        User result = userService.getUserById(user.getId());

        assertEquals(user.getId(), result.getId());
    }

    @Test
    void getAll() {
        List<User> list = List.of(user, admin);

        when(userRepository.getAll()).thenReturn(list);

        List<User> all = userService.getAll();

        assertEquals(2, all.size());
    }
}
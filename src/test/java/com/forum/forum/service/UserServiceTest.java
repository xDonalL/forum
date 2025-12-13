package com.forum.forum.service;

import com.forum.forum.model.User;
import com.forum.forum.repository.user.UserRepository;
import com.forum.forum.to.RegistrationUserTo;
import com.forum.forum.util.exception.EmailAlreadyExistsException;
import com.forum.forum.util.exception.LoginAlreadyExistsException;
import com.forum.forum.util.exception.PasswordMismatchException;
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

    @Test
    void registerValidNewUser() {
        User newUser = getNew();
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User registerUser = userService.register(new RegistrationUserTo(
                newUser.getEmail(), newUser.getLogin(), newUser.getPassword(), newUser.getPassword()));

        assertNotNull(newUser);
        assertEquals(newUser.getName(), registerUser.getName());
        assertEquals(newUser.getRoles().toString(), registerUser.getRoles().toString());
    }

    @Test
    void registerEmailAlreadyExistException() {
        User existing = getNew();

        when(userRepository.getByEmail(existing.getEmail())).thenReturn(existing);

        User newUser = getNew();
        newUser.setEmail(existing.getEmail());

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.register(new RegistrationUserTo(newUser)));

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerLoginAlreadyExistException() {
        User existing = getNew();
        when(userRepository.getByLogin(existing.getLogin())).thenReturn(existing);

        User newUser = getNew();
        newUser.setLogin(existing.getLogin());

        assertThrows(LoginAlreadyExistsException.class,
                () -> userService.register(new RegistrationUserTo(newUser)));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerPasswordMismatchException() {
        assertThrows(PasswordMismatchException.class,
                () -> userService.register(new RegistrationUserTo(
                        USER.getEmail(), USER.getLogin(), "password", "confirmPassword")));

        verify(userRepository, never()).save(any());
    }
}
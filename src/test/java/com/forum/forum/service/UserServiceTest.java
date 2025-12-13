package com.forum.forum.service;

import com.forum.forum.model.User;
import com.forum.forum.repository.user.UserRepository;
import com.forum.forum.to.RegistrationUserTo;
import com.forum.forum.util.exception.EmailAlreadyExistsException;
import com.forum.forum.util.exception.LoginAlreadyExistsException;
import com.forum.forum.util.exception.NotFoundException;
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
    void createUserSuccess() {
        User newUser = getNew();
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User saved = userService.createUser(newUser);

        assertNotNull(saved);
        assertEquals(newUser.getName(), saved.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserException() {
        User updateUser = getUpdated();

        when(userRepository.get(USER_ID)).thenReturn(USER);
        when(userRepository.save(any(User.class))).thenReturn(updateUser);

        User saved = userService.update(updateUser);

        assertNotNull(saved);
        assertEquals(updateUser.getName(), saved.getName());
        assertEquals(updateUser.getRoles(), saved.getRoles());

        verify(userRepository).get(USER_ID);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserNotFoundException() {
        when(userRepository.save(any(User.class))).thenReturn(null);

    }

    @Test
    void deleteUserSuccess() {
        when(userRepository.delete(USER_ID)).thenReturn(true);

        boolean deleted = userService.delete(USER_ID);

        assertTrue(deleted);
        verify(userRepository).delete(USER_ID);
    }

    @Test
    void deleteUserNotFoundException() {
        when(userRepository.delete(NOT_FOUND_ID)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.delete(NOT_FOUND_ID));
    }

    @Test
    void getByEmailSuccess() {
        when(userRepository.getByEmail(USER.getEmail())).thenReturn(USER);

        User result = userService.getByEmail(USER.getEmail());

        assertEquals(USER.getEmail(), result.getEmail());
    }

    @Test
    void getByEmailNotFoundException() {
        when(userRepository.getByEmail(NOT_FOUND_EMAIL)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> userService.getByEmail(NOT_FOUND_EMAIL));
    }

    @Test
    void getUserByIdSuccess() {
        when(userRepository.get(USER.getId())).thenReturn(USER);

        User result = userService.getUserById(USER_ID);

        assertEquals(USER_ID, result.getId());
    }

    @Test
    void getUserByIdNotFoundException() {
        when(userRepository.get(NOT_FOUND_ID)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.getUserById(NOT_FOUND_ID));
    }

    @Test
    void getAllUserSuccess() {
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
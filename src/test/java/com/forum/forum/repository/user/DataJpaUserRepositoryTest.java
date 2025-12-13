package com.forum.forum.repository.user;

import com.forum.forum.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.forum.forum.UserTestData.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DataJpaUserRepository.class)
@ActiveProfiles("test")
class DataJpaUserRepositoryTest {

    @Autowired
    private DataJpaUserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(getNew());
    }

    @Test
    void save() {
        assertNotNull(savedUser.getId());
        assertEquals(savedUser.getEmail(), getNew().getEmail());
    }

    @Test
    void delete() {
        assertTrue(userRepository.delete(savedUser.getId()));
    }

    @Test
    void getById() {
        User found = userRepository.get(savedUser.getId());
        assertNotNull(found);
        assertEquals(savedUser.getId(), found.getId());
    }

    @Test
    void getByEmail() {
        User found = userRepository.getByEmail(savedUser.getEmail());
        assertNotNull(found);
        assertEquals(savedUser.getEmail(), found.getEmail());
    }

    @Test
    void getAll() {
        ADMIN.setId(null);
        userRepository.save(ADMIN);

        List<User> users = userRepository.getAll();
        assertEquals(ALL_USERS.size(), users.size());
    }
}
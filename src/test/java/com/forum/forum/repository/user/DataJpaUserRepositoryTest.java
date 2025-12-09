package com.forum.forum.repository.user;

import com.forum.forum.UserTestData;
import com.forum.forum.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DataJpaUserRepository.class)
@ActiveProfiles("test")
class DataJpaUserRepositoryTest {

    @Autowired
    private DataJpaUserRepository userRepository;

    @Test
    void save() {
        User saved = userRepository.save(UserTestData.getNew());

        assertNotNull(saved.getId());
        assertEquals(UserTestData.getNew().getEmail(), saved.getEmail());
    }

    @Test
    void delete() {
        User saved = userRepository.save(UserTestData.getNew());

        assertTrue(userRepository.delete(saved.getId()));
    }

    @Test
    void get() {
        User saved = userRepository.save(UserTestData.getNew());

        User found = userRepository.get(saved.getId());
        assertNotNull(found);
        assertEquals(UserTestData.getNew().getEmail(), found.getEmail());
    }

    @Test
    void getByEmail() {
        User newUser = UserTestData.getNew();
        userRepository.save(newUser);

        User found = userRepository.getByEmail(newUser.getEmail());
        assertNotNull(found);
        assertEquals(UserTestData.getNew().getEmail(), found.getEmail());
    }

    @Test
    void getAll() {
        userRepository.save(UserTestData.USER);
        userRepository.save(UserTestData.ADMIN);

        List<User> users = userRepository.getAll();
        assertEquals(2, users.size());
    }
}
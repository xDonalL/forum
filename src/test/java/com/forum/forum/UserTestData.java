package com.forum.forum;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;

import java.util.List;

public class UserTestData {

    public static final Integer USER_ID = 1;
    public static final Integer ADMIN_ID = 2;
    public static final Integer MODER_ID = 3;

    public static final Integer NOT_FOUND_ID = 100;
    public static final String NOT_FOUND_EMAIL = "notFoundEmail";

    public static final User USER = new User(USER_ID, "user@yandex.ru", "user", "password", Role.USER);
    public static final User ADMIN = new User(ADMIN_ID, "admin@gmail.com", "admin", "admin", Role.ADMIN, Role.USER);
    public static final User MODER = new User(MODER_ID, "moder@gmail.com", "admin", "admin", Role.MODERATOR, Role.USER);

    public static final List<User> ALL_USERS = List.of(USER, ADMIN);

    public static User getNew() {
        return new User("newEmail@gmail.com", "newUser", "newPassword", Role.USER);
    }

    public static User getUpdated() {
        User updated = USER;

        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setRoles(Role.ADMIN);
        return updated;
    }
}

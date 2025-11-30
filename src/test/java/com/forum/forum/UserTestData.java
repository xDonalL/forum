package com.forum.forum;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;

public class UserTestData {

    public static final User user = new User("user@yandex.ru", "user", "password", Role.USER);
    public static final User admin = new User("admin@gmail.com", "admin", "admin", Role.ADMIN, Role.USER);

    public static User getNew() {
        return new User("newEmail@gmail.com", "newUser", "newPassword", Role.USER);
    }

    public static User getUpdated() {
        User updated = new User(user);

        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Role.ADMIN);
        return updated;
    }
}

package com.forum.forum.repository.user;

import com.forum.forum.model.User;
import com.forum.forum.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User> {

    User getByEmail(String email);

    User getByLogin(String login);

}

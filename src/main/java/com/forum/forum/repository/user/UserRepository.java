package com.forum.forum.repository.user;

import com.forum.forum.model.User;
import com.forum.forum.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends BaseRepository<User> {

    User getByEmail(String email);

    User getByLogin(String login);

    Page<User> getAll(Pageable pageable);

    Page<User> getByRole(Pageable pageable, String role);

    Page<User> getBanned(Pageable pageable);

    Page<User> getByContainingEmail(Pageable pageable, String email);

    Page<User> getByContainingLogin(Pageable pageable, String login);
}

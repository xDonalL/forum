package com.forum.forum.repository.user;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DataJpaUserRepository implements UserRepository {

    private final CrudUserRepository crudRepository;

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    @Override
    public User getByLogin(String login) {
        return crudRepository.getByLogin(login);
    }

    public Page<User> getAll(Pageable pageable) {
        return crudRepository.findAllByOrderByRegisteredAtDesc(pageable);
    }

    public Page<User> getByRole(Pageable pageable, String role) {
        return crudRepository.findByRoles(pageable, Role.valueOf(role.toUpperCase()));
    }

    public Page<User> getBanned(Pageable pageable) {
        return crudRepository.findByEnabledFalse(pageable);
    }

    public Page<User> getByContainingEmail(Pageable pageable, String email) {
        return crudRepository.findByEmailContainingIgnoreCase(pageable, email);
    }

    public Page<User> getByContainingLogin(Pageable pageable, String login) {
        return crudRepository.findByLoginContainingIgnoreCase(pageable, login);
    }
}

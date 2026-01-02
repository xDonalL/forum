package com.forum.forum.repository.user;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataJpaUserRepository implements UserRepository {

    private final CrudUserRepository crudRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

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

    public List<User> getAll() {
        return crudRepository.findAllByOrderByRegisteredAtDesc();
    }

    public List<User> getByRole(String role) {
        return crudRepository.findByRoles(Role.valueOf(role.toUpperCase()));
    }

    public List<User> getBanned() {
        return crudRepository.findByEnabledFalse();
    }

    public List<User> getByContainingEmail(String email) {
        return crudRepository.findByEmailContainingIgnoreCase(email);
    }

    public List<User> getByContainingLogin(String login) {
        return crudRepository.findByLoginContainingIgnoreCase(login);
    }
}

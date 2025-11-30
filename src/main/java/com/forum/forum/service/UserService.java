package com.forum.forum.service;

import com.forum.forum.model.User;
import com.forum.forum.repository.user.UserRepository;
import com.forum.forum.util.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        ValidUtil.checkIsNew(user);
        return userRepository.save(user);
    }

    public boolean delete(int id) {
        ValidUtil.checkNotFound(id, "user with id= " + id + " not exist");
        return userRepository.delete(id);
    }

    public User getByEmail(String email) {
        ValidUtil.checkNotFound(email, "user with email= " + email + " not exist");
        return userRepository.getByEmail(email);
    }

    public User getUserById(int id) {
        ValidUtil.checkNotFound(id, "user with id= " + id + " not exist");
        return userRepository.get(id);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }
}

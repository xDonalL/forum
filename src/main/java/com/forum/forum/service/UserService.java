package com.forum.forum.service;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;
import com.forum.forum.repository.user.UserRepository;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.util.ValidUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initUser() {
        userRepository.save(new User(
                "user@mail.com", "name",
                passwordEncoder.encode("password"), Role.USER));
    }

    public User createUser(User user) {
        ValidUtil.checkIsNew(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}

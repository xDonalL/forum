package com.forum.forum.service;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;
import com.forum.forum.repository.user.DataJpaUserRepository;
import com.forum.forum.security.AuthorizedUser;
import com.forum.forum.to.RegistrationUserTo;
import com.forum.forum.util.ValidUtil;
import com.forum.forum.util.exception.EmailAlreadyExistsException;
import com.forum.forum.util.exception.LoginAlreadyExistsException;
import com.forum.forum.util.exception.PasswordMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private DataJpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user) {
        ValidUtil.checkIsNew(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean delete(int id) {
        return checkNotFound(userRepository.delete(id), "user with id= " + id + " not exist");
    }

    public User getByEmail(String email) {
        return checkNotFound(userRepository.getByEmail(email), "user with email= " + email + " not exist");
    }

    public User getUserById(int id) {
        return checkNotFound(userRepository.get(id), "user with id= " + id + " not exist");
    }

    public List<User> filterUsers(String filter) {
        List<User> users;

        if (filter == null) {
            return users = userRepository.getAll();
        }
        switch (filter) {
            case "banned":
                users = userRepository.getBanned();
                break;
            case "admin":
                users = userRepository.getByRole(filter);
                break;
            case "moderator":
                users = userRepository.getByRole(filter);
                break;
            default:
                users = userRepository.getAll();
        }
        return users;
    }

    public List<User> search(String q, String type) {
        List<User> users;
        if (type.equals("email")) {
            return users = userRepository.getByContainingEmail(q);
        } else {
            return users = userRepository.getByContainingLogin(q);
        }
    }

    public User update(User user, MultipartFile avatarFile) throws IOException {
        checkNotFound(userRepository.get(user.getId()), "user with id= " + user.getId() + " not exist");

        if (avatarFile != null && !avatarFile.isEmpty()) {
            saveAvatar(user, avatarFile);
        }

        return userRepository.save(user);
    }

    private void saveAvatar(User user, MultipartFile avatarFile) throws IOException {
        String filename = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/avatars");

        Files.createDirectories(uploadPath);
        avatarFile.transferTo(uploadPath.resolve(filename));

        user.setAvatar(filename);
    }

    public User register(RegistrationUserTo registrationTo) {
        if (!registrationTo.getPassword().equals(registrationTo.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }
        if (userRepository.getByEmail(registrationTo.getEmail()) != null) {
            throw new EmailAlreadyExistsException(registrationTo.getEmail());
        }
        if (userRepository.getByLogin(registrationTo.getLogin()) != null) {
            throw new LoginAlreadyExistsException(registrationTo.getLogin());
        }

        return create(new User(
                registrationTo.getEmail(),
                registrationTo.getLogin(),
                registrationTo.getPassword(),
                Role.USER
        ));
    }

    @Transactional
    public void banUser(int id) {
        User user = userRepository.get(id);
        checkNotFound(user, "user with id= " + user.getId() + " not exist");
        user.setEnabled(false);
    }

    @Transactional
    public void unbanUser(int id) {
        User user = userRepository.get(id);
        checkNotFound(user, "user with id= " + user.getId() + " not exist");
        user.setEnabled(true);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    public User getCurrentUser() {
        AuthorizedUser auth = (AuthorizedUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.get(auth.getUser().getId());
    }
}

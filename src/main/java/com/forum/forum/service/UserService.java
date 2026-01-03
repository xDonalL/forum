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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private DataJpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user) {
        log.debug("Creating user: email={}, login={}", user.getEmail(), user.getLogin());

        ValidUtil.checkIsNew(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);

        log.info("User created: userId={}, email={}", saved.getId(), saved.getEmail());
        return saved;
    }

    public boolean delete(int id) {
        log.debug("Deleting user: id={}", id);

        boolean deleted = checkNotFound(userRepository.delete(id),
                "user with id=" + id + " not exist");

        log.info("User deleted: userId={}", id);
        return deleted;
    }

    @Cacheable("profile")
    public User getByEmail(String email) {
        log.debug("Getting user by email: {}", email);
        return checkNotFound(userRepository.getByEmail(email),
                "user with email=" + email + " not exist");
    }

    @Cacheable("profile")
    public User getUserById(int id) {
        log.debug("Getting user by id: {}", id);
        return checkNotFound(userRepository.get(id),
                "user with id=" + id + " not exist"
        );
    }

    public List<User> filterUsers(String filter) {
        log.debug("Filtering users by '{}'", filter);

        if (filter == null) {
            return userRepository.getAll();
        }

        return switch (filter) {
            case "banned" -> userRepository.getBanned();
            case "admin", "moderator" -> userRepository.getByRole(filter);
            default -> userRepository.getAll();
        };
    }

    public List<User> search(String q, String type) {
        log.debug("Searching users: query='{}', type={}", q, type);

        List<User> users = type.equals("email")
                ? userRepository.getByContainingEmail(q)
                : userRepository.getByContainingLogin(q);

        log.info("User search completed: query='{}', found={}", q, users.size());
        return users;
    }

    @CacheEvict(value = "profile", allEntries = true)
    @PreAuthorize("@topicSecurity.isOwner(#user.id)")
    public User update(User user, MultipartFile avatarFile) throws IOException {
        log.debug("Updating user: userId={}", user.getId());

        checkNotFound(userRepository.get(user.getId()),
                "user with id=" + user.getId() + " not exist");

        if (avatarFile != null && !avatarFile.isEmpty()) {
            log.debug("Updating avatar: userId={}, filename={}",
                    user.getId(), avatarFile.getOriginalFilename());
            saveAvatar(user, avatarFile);
        }

        User updated = userRepository.save(user);

        log.info("User updated: userId={}", updated.getId());
        return updated;
    }

    private void saveAvatar(User user, MultipartFile avatarFile) throws IOException {
        String filename = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
        Path uploadPath = Paths.get("uploads/avatars");

        Files.createDirectories(uploadPath);
        avatarFile.transferTo(uploadPath.resolve(filename));

        user.setAvatar(filename);

        log.info("Avatar saved: userId={}, file={}", user.getId(), filename);
    }

    public User register(RegistrationUserTo registrationTo) {
        log.debug("Registering user: email={}, login={}",
                registrationTo.getEmail(), registrationTo.getLogin());

        if (!registrationTo.getPassword().equals(registrationTo.getConfirmPassword())) {
            log.warn("Password mismatch during registration: email={}", registrationTo.getEmail());
            throw new PasswordMismatchException();
        }

        if (userRepository.getByEmail(registrationTo.getEmail()) != null) {
            log.warn("Email already exists: {}", registrationTo.getEmail());
            throw new EmailAlreadyExistsException(registrationTo.getEmail());
        }

        if (userRepository.getByLogin(registrationTo.getLogin()) != null) {
            log.warn("Login already exists: {}", registrationTo.getLogin());
            throw new LoginAlreadyExistsException(registrationTo.getLogin());
        }

        User user = create(new User(
                registrationTo.getEmail(),
                registrationTo.getLogin(),
                registrationTo.getPassword(),
                Role.USER
        ));

        log.info("User registered: userId={}, email={}", user.getId(), user.getEmail());
        return user;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @Transactional
    public void banUser(int id) {
        log.debug("Banning user: userId={}", id);

        User user = userRepository.get(id);
        checkNotFound(user, "user with id=" + id + " not exist");

        user.setEnabled(false);
        log.info("User banned: userId={}", id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @Transactional
    public void unbanUser(int id) {
        log.debug("Unbanning user: userId={}", id);

        User user = userRepository.get(id);
        checkNotFound(user, "user with id=" + id + " not exist");

        user.setEnabled(true);
        log.info("User unbanned: userId={}", id);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Authenticating user: email={}", email);

        User user = userRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            log.warn("Authentication failed: email={}", email);
            throw new UsernameNotFoundException("User " + email + " is not found");
        }

        log.info("User authenticated: userId={}, email={}", user.getId(), email);
        return new AuthorizedUser(user);
    }

    public User getCurrentUser() {
        AuthorizedUser auth = (AuthorizedUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User user = userRepository.get(auth.getUser().getId());

        log.debug("Current user resolved: userId={}", user.getId());
        return user;
    }
}

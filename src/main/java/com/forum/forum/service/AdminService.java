package com.forum.forum.service;

import com.forum.forum.model.User;
import com.forum.forum.repository.user.DataJpaUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.forum.forum.util.ValidUtil.checkNotFound;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private DataJpaUserRepository userRepository;

    public Page<User> filterUsers(int page, int size, String filter) {
        log.debug("Filtering users by '{}'", filter);

        Pageable pageable = PageRequest.of(page, size);

        if (filter == null) {
            return userRepository.getAll(pageable);
        }

        return switch (filter) {
            case "banned" -> userRepository.getBanned(pageable);
            case "admin", "moderator" -> userRepository.getByRole(pageable, filter);
            default -> userRepository.getAll(pageable);
        };
    }

    public Page<User> search(int page, int size, String q, String type) {
        log.debug("Searching users: query='{}', type={}", q, type);

        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = type.equals("email")
                ? userRepository.getByContainingEmail(pageable, q)
                : userRepository.getByContainingLogin(pageable, q);

        log.info("User search completed: query='{}', found={}", q, users.getTotalElements());
        return users;
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
}

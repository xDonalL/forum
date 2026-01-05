package com.forum.forum.repository.user;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CrudUserRepository extends JpaRepository<User, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    Page<User> findByRoles(Pageable pageable, Role role);

    User getByEmail(String email);

    User getByLogin(String login);

    @EntityGraph(attributePaths = "roles")
    Page<User> findAllByOrderByRegisteredAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = "roles")
    Page<User> findByEnabledFalse(Pageable pageable);

    @EntityGraph(attributePaths = "roles")
    Page<User> findByLoginContainingIgnoreCase(Pageable pageable, String q);

    @EntityGraph(attributePaths = "roles")
    Page<User> findByEmailContainingIgnoreCase(Pageable pageable, String q);
}

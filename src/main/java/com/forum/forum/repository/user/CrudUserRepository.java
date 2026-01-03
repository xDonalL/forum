package com.forum.forum.repository.user;

import com.forum.forum.model.Role;
import com.forum.forum.model.User;
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

    List<User> findByRoles(Role role);

    User getByEmail(String email);

    User getByLogin(String login);

    @EntityGraph(attributePaths = "roles")
    List<User> findAllByOrderByRegisteredAtDesc();

    @EntityGraph(attributePaths = "roles")
    List<User> findByEnabledFalse();

    @EntityGraph(attributePaths = "roles")
    List<User> findByLoginContainingIgnoreCase(String q);

    @EntityGraph(attributePaths = "roles")
    List<User> findByEmailContainingIgnoreCase(String q);
}

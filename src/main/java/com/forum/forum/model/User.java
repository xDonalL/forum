package com.forum.forum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    public User(String email, String login, String password, Role... role) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.enabled = true;
        this.roles.addAll(Arrays.asList(role));
    }

    public User(Integer id, String email, String login, String password, Role... role) {
        this(email, login, password, role);
        this.id = id;
    }

    public User(User user) {
        this(user.id, user.email, user.login, user.password, user.roles.toArray(new Role[0]));
    }

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "name")
    private String name = "Name";

    @Nullable
    @Column(name = "avatar")
    private String avatar = "new_user.jpg";

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "register", nullable = false, updatable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    public void setRoles(Role roles) {
        this.roles.add(roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "email='" + email + '\'' +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", registeredAt=" + registeredAt +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return enabled == user.enabled && Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(registeredAt, user.registeredAt) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, name, password, enabled, registeredAt, roles);
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    public boolean isModerator() {
        return roles.contains(Role.MODERATOR);
    }
}

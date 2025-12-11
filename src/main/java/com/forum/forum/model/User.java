package com.forum.forum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractBaseEntity {

    public User(String email, String name, String password, Role... role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.enabled = true;
        this.roles.addAll(Arrays.asList(role));
    }

    public User(Integer id, String email, String name, String password, Role... role) {
        this(email, name, password, role);
        this.id = id;
    }

    public User(User user) {
        this(user.id, user.email, user.name, user.password, user.roles.toArray(new Role[0]));
    }

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Nullable
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "register", nullable = false, updatable = false)
    private LocalDateTime register = LocalDateTime.now();

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
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", register=" + register +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return enabled == user.enabled && Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(register, user.register) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, name, password, enabled, register, roles);
    }
}

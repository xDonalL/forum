package com.forum.forum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

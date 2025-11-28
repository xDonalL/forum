package com.forum.forum.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User extends AbstractBaseEntity {

    public User(String email, String name, String password, Role role) {
        this.email = email;
        this.name = name;
        this.password = password;
        enabled = true;
        roles.add(role);
    }

    private String email;

    private String name;

    private String password;

    private boolean enabled;

    private final Date register = new Date();

    private Set<Role> roles = new HashSet<>();
}

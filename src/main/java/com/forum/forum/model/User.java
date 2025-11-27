package com.forum.forum.model;

import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Getter
public class User extends AbstractBaseEntity {

    private String email;

    private String name;

    private String password;

    private boolean enabled;

    private final Date register = new Date();

    private Set<Role> roles;
}

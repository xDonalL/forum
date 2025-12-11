package com.forum.forum.to;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationUserTo {
    private String email;
    private String login;
    private String password;
    private String confirmPassword;
}

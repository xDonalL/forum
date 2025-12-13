package com.forum.forum.to;

import com.forum.forum.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RegistrationUserTo {

    public RegistrationUserTo(User user) {
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.confirmPassword = user.getPassword();
    }

    private String email;
    private String login;
    private String password;
    private String confirmPassword;
}

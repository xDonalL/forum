package com.forum.forum.to;

import com.forum.forum.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "{validation.notBlank}")
    @Email(message = "{validation.email}")
    private String email;

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 4, max = 20, message = "{validation.login.size}")
    @Pattern(
            regexp = "^(?!\\d)[^\\s]+$",
            message = "{validation.login.pattern}"
    )
    private String login;

    @NotBlank(message = "{validation.notBlank}")
    @Size(min = 6, message = "{validation.password.min}")
    private String password;

    @NotBlank(message = "{validation.notBlank}")
    private String confirmPassword;
}

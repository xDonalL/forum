package com.forum.forum.to;

import com.forum.forum.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "should not be empty")
    @Email(message = "Incorrect email")
    private String email;

    @NotBlank(message = "should not be empty")
    @Size(min = 4, max = 20, message = "Login from 4 to 20 character")
    private String login;

    @NotBlank(message = "should not be empty")
    @Size(min = 6, message = "Password min 6 character")
    private String password;

    @NotBlank(message = "should not be empty")
    private String confirmPassword;
}

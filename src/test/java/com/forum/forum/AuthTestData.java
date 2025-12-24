package com.forum.forum;

import com.forum.forum.model.User;
import com.forum.forum.security.AuthorizedUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthTestData {
    public static UsernamePasswordAuthenticationToken getAuthToken(User user) {
        AuthorizedUser auth = new AuthorizedUser(user);
        return new UsernamePasswordAuthenticationToken(
                auth, null, auth.getAuthorities());
    }
}

package com.openklaster.app.model;

import com.openklaster.app.model.entities.user.UserEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collections;

public class AuthenticatedUser extends AbstractAuthenticationToken {
    private final UserEntity user;
    private final String usedToken;

    public AuthenticatedUser(UserEntity user, String token) {
        super(Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
        this.user = user;
        this.usedToken = token;
    }

    @Override
    public Object getCredentials() {
        return usedToken;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}

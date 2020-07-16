package com.openklaster.core.vertx.authentication;

import com.openklaster.common.model.UserToken;
import lombok.Getter;

public class SuccessfulSessionAuthentication implements AuthenticationResult {

    @Getter
    private final UserToken sessionToken;

    @Override
    public boolean succeeded() {
        return true;
    }

    @Override
    public Throwable getCause() {
        return null;
    }

    public SuccessfulSessionAuthentication(UserToken sessionToken) {
        this.sessionToken = sessionToken;
    }

}

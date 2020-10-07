package com.openklaster.core.vertx.authentication;

import com.openklaster.common.model.SessionToken;
import com.openklaster.common.model.UserToken;
import lombok.Getter;

public class SuccessfulSessionAuthentication implements AuthenticationResult {

    @Getter
    private final SessionToken sessionToken;

    @Override
    public boolean succeeded() {
        return true;
    }

    @Override
    public Exception getCause() {
        return null;
    }

    public SuccessfulSessionAuthentication(SessionToken sessionToken) {
        this.sessionToken = sessionToken;
    }

}

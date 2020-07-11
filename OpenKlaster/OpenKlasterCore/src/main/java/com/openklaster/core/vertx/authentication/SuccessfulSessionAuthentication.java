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

    public SuccessfulSessionAuthentication(UserToken sessionToken) {
        this.sessionToken = sessionToken;
    }

}

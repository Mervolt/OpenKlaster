package com.openklaster.common.authentication;

public class AuthenticationClient {
    private final ITokenGenerator tokenGenerator;
    private final PasswordHandler passwordHandler;

    public AuthenticationClient(ITokenGenerator tokenGenerator, PasswordHandler passwordHandler) {
        this.tokenGenerator = tokenGenerator;
        this.passwordHandler = passwordHandler;
    }

}

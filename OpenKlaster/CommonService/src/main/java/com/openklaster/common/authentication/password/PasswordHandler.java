package com.openklaster.common.authentication.password;

public interface PasswordHandler {

    String hashPassword(String plainPassword);

    boolean authenticatePassword(String plainPassword, String hashedPassword);
}

package com.openklaster.common.authentication;

public interface PasswordHandler {

    String hashPassword(String plainPassword);

    boolean authenticatePassword(String plainPassword, String hashedPassword);
}

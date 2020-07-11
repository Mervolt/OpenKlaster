package com.openklaster.common.authentication.password;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordHandler implements PasswordHandler {
    @Override
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    @Override
    public boolean authenticatePassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

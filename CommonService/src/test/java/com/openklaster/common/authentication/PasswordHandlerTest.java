package com.openklaster.common.authentication;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.Assert.*;

public class PasswordHandlerTest {

    @Test
    public void TestBasic(){
        String password = "somePASSWORDMAN";
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());
        String hashedPassword2 = BCrypt.hashpw(password,BCrypt.gensalt());

        assertTrue(BCrypt.checkpw(password,hashedPassword));
        assertTrue(BCrypt.checkpw(password,hashedPassword2));
        assertNotEquals(hashedPassword,hashedPassword2);

    }

}
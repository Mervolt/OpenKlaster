package com.openklaster.core.service.users;

import java.util.Arrays;
import java.util.List;

public class ManagerContainer {
    private final LoginManager loginManager;
    private final RegisterManager registerManager;
    private final UpdateUserManager updateUserManager;

    public ManagerContainer(LoginManager loginManager, RegisterManager registerManager, UpdateUserManager updateUserManager) {
        this.loginManager = loginManager;
        this.registerManager = registerManager;
        this.updateUserManager = updateUserManager;
    }

    public List<UserManager> retrieveManagers() {
        return Arrays.asList(loginManager, registerManager, updateUserManager);
    }
}

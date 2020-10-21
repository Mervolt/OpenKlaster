package com.openklaster.core.service.users;

import java.util.Arrays;
import java.util.List;

public class ManagerContainerHelper {
    private final InformationManager informationManager;
    private final GenerateTokenManager generateTokenManager;
    private final DeleteTokenManager deleteTokenManager;
    private final DeleteAllTokensManager deleteAllTokensManager;

    public ManagerContainerHelper(InformationManager informationManager, GenerateTokenManager generateTokenManager,
                                  DeleteTokenManager deleteTokenManager, DeleteAllTokensManager deleteAllTokensManager) {
        this.informationManager = informationManager;
        this.generateTokenManager = generateTokenManager;
        this.deleteTokenManager = deleteTokenManager;
        this.deleteAllTokensManager = deleteAllTokensManager;
    }

    public List<UserManagerHelper> retrieveManagerHelpers() {
        return Arrays.asList(informationManager, generateTokenManager, deleteTokenManager, deleteAllTokensManager);
    }
}

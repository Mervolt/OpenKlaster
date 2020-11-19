package com.openklaster.filerepository.app;

import com.openklaster.common.verticle.OpenklasterVerticleLauncher;

public class FileRepositoryVerticleLauncher extends OpenklasterVerticleLauncher {
    public static void main(String[] args) {
        launchVerticle(new FileRepositoryVerticle());
    }
}

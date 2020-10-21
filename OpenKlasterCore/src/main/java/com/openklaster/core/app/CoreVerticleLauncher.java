package com.openklaster.core.app;

import com.openklaster.common.verticle.OpenklasterVerticleLauncher;

public class CoreVerticleLauncher extends OpenklasterVerticleLauncher {
    public static void main(String[] args) {
        launchVerticle(new CoreVerticle(true));
    }
}

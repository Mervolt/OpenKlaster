package com.openklaster.api.app;

import com.openklaster.common.verticle.OpenklasterVerticleLauncher;

public class ApiVerticleLauncher extends OpenklasterVerticleLauncher {
    public static void main(String[] args) {
      launchVerticle(new ApiVerticle(true));
    }
}

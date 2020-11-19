package com.openklaster.mongo.app;

import com.openklaster.common.verticle.OpenklasterVerticleLauncher;

public class MongoVerticleLauncher extends OpenklasterVerticleLauncher {
    public static void main(String[] args) {
        launchVerticle(new MongoVerticle());
    }
}

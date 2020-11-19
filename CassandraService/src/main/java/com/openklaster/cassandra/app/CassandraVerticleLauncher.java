package com.openklaster.cassandra.app;

import com.openklaster.common.verticle.OpenklasterVerticleLauncher;

public class CassandraVerticleLauncher extends OpenklasterVerticleLauncher {
    public static void main(String[] args) {
        launchVerticle(new CassandraVerticle());
    }
}

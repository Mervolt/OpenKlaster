package com.openklaster.common.verticle;

import io.vertx.core.AbstractVerticle;

public abstract class OpenklasterVerticle extends AbstractVerticle {
    protected boolean isDevModeOn;
    protected String configFilenamePrefix;

    public OpenklasterVerticle() {
        this.isDevModeOn = false;
        setConfigFilenamePrefix(false);
    }

    public OpenklasterVerticle(boolean isDevModeOn) {
        this.isDevModeOn = isDevModeOn;
        setConfigFilenamePrefix(isDevModeOn);
    }

    private void setConfigFilenamePrefix(boolean isDevModeOn) {
        this.configFilenamePrefix = isDevModeOn ? "config-dev" : "config";
    }
}

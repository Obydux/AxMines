package com.artillexstudios.axmines.config.impl;

import com.artillexstudios.axmines.config.AbstractConfig;
import com.artillexstudios.axmines.utils.FileUtils;

public class Config extends AbstractConfig {

    @Key("debug")
    @Comment("""
            Whether to send debug messages to the console
            debug: false\
            """)
    public static boolean DEBUG = false;

    private static final Config CONFIG = new Config();

    public static void reload() {
        CONFIG.reload(FileUtils.PLUGIN_DIRECTORY.resolve("config.yml"), Config.class, null, null);
    }
}

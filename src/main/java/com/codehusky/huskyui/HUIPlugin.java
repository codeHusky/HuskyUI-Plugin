package com.codehusky.huskyui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import javax.inject.Inject;

/**
 * Created by lokio on 6/27/2017.
 */
@Plugin(id = "huskyui", name = "HuskyUI", version = "0.1.0", description = "A UI framework.")
public class HUIPlugin {
    private static HUIPlugin instance;

    @Inject private PluginContainer pC;
    @Inject private Logger logger;

    private Cause genericCause;

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        instance = this;
        logger.info("HuskyUI is loaded and installed.");
    }

    @Listener
    public void gameStart(GameStartedServerEvent event) {
        logger.info("HuskyUI is now running.");
        genericCause = Cause.builder().owner(pC).build();
    }

    public static HUIPlugin getInstance() {
        return instance;
    }

    public Cause getGenericCause() {
        return genericCause;
    }
}

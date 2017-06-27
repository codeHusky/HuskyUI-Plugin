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
@Plugin(id="huskyui",name="HuskyUI",version = "0.1.0",description = "A UI framework.")
public class HUIPlugin {
    public static HUIPlugin instance;

    @Inject
    public PluginContainer pC;
    public Logger logger;
    public Cause genericCause;
    @Listener
    public void preinit(GamePreInitializationEvent event){
        instance = this;
        logger = LoggerFactory.getLogger(pC.getName());
        logger.info("HuskyUI is loaded and installed.");
    }
    @Listener
    public void gameStart(GameStartedServerEvent event){
        logger.info("HuskyUI is now running.");
        genericCause = Cause.of(NamedCause.of("PluginContainer",pC));
    }
}

/*
 * This file is part of HuskyUI.
 *
 * HuskyUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HuskyUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HuskyUI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codehusky.huskyui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@Plugin(id = HuskyUI.PLUGIN_ID, name = HuskyUI.PLUGIN_NAME, version = HuskyUI.PLUGIN_VERSION,
        description = "A framework for Inventory UI.")
public class HuskyUI {

    public static final String PLUGIN_ID = "huskyui";
    public static final String PLUGIN_NAME = "HuskyUI";
    public static final String PLUGIN_VERSION = "0.2.1";

    private static final Logger LOGGER = LoggerFactory.getLogger(HuskyUI.class);

    private static HuskyUI instance;

    @Nonnull private final PluginContainer pluginContainer;
    private Cause genericCause;

    @Inject
    public HuskyUI(@Nonnull final PluginContainer pluginContainer) {
        HuskyUI.instance = this;
        this.pluginContainer = pluginContainer;
    }

    public static HuskyUI getInstance() {
        return HuskyUI.instance;
    }

    @Nonnull
    public PluginContainer getPluginContainer() {
        return this.pluginContainer;
    }

    public Cause getGenericCause() {
        return this.genericCause;
    }

    @Listener
    public void onGameStartedServer(@Nonnull final GameStartedServerEvent event) {
        this.genericCause = Cause.of(NamedCause.of("PluginContainer", this.pluginContainer));
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}

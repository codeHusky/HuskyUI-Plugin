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
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * The HuskyUI class that gets loaded by Sponge at runtime.
 *
 * <p>Currently this class serves no real purpose except as
 * an easily accessible container for versioning and logging
 * data.</p>
 */
@Plugin(id = HuskyUI.PLUGIN_ID, name = HuskyUI.PLUGIN_NAME, version = HuskyUI.PLUGIN_VERSION,
        description = HuskyUI.PLUGIN_DESCRIPTION)
public class HuskyUI {

    /**
     * The ID of HuskyUI for Sponge.
     */
    public static final String PLUGIN_ID = "@pluginId@";

    /**
     * Creates a unique ID for HuskyUI to use for its generic {@link Cause}.
     */
    public static final String CAUSE_STRING = "@pluginId@-cause";

    /**
     * Defines the type of class that the generic {@link Cause} must take.
     */
    public static final Class<PluginContainer> CAUSE_CLASS = PluginContainer.class;

    /**
     * The Name of HuskyUI for Sponge.
     */
    public static final String PLUGIN_NAME = "@pluginName@";

    /**
     * The Version of HuskyUI for Sponge.
     */
    public static final String PLUGIN_VERSION = "@pluginVersion@";

    /**
     * The Description of HuskyUI for Sponge.
     */
    public static final String PLUGIN_DESCRIPTION = "@pluginDescription@";

    /**
     * The HuskyUI {@link Logger} used throughout the plugin.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HuskyUI.class);

    /**
     * Contains a reference to this (soft) singleton class.
     */
    private static HuskyUI instance;

    /**
     * References the {@link PluginContainer} that Sponge
     * creates for HuskyUI.
     */
    @Nonnull private final PluginContainer pluginContainer;

    /**
     * The main, generic {@link Cause} that gets passed to
     * Sponge during (or while creating) events.
     */
    @Nonnull private final Cause genericCause;

    /**
     * The HuskyUI constructor.
     *
     * <p>Is intended to be called by Sponge's classloader.</p>
     *
     * @param pluginContainer the container for HuskyUI passed by Sponge
     */
    @Inject
    public HuskyUI(@Nonnull final PluginContainer pluginContainer) {
        HuskyUI.instance = this;
        this.pluginContainer = pluginContainer;
        this.genericCause = Cause.of(NamedCause.of(CAUSE_STRING, this.pluginContainer));
    }

    /**
     * Gets an instance of HuskyUI.
     *
     * @return an instance of HuskyUI
     */
    public static HuskyUI getInstance() {
        return HuskyUI.instance;
    }

    /**
     * Gets HuskyUI's plugin container created by Sponge.
     *
     * @return HuskyUI's plugin container
     */
    @Nonnull
    public PluginContainer getPluginContainer() {
        return this.pluginContainer;
    }

    /**
     * Gets the generic cause that HuskyUI uses during (or while starting) events.
     *
     * @return HuskyUI's generic event cause
     */
    @Nonnull
    public Cause getGenericCause() {
        return this.genericCause;
    }

    /**
     * Determines whether or not a given cause contains HuskUI.
     *
     * @param cause the cause being looked at
     * @return true if HuskyUI is a cause in the given cause; false otherwise
     */
    public boolean isCause(@Nonnull final Cause cause) {
        final PluginContainer container = cause.get(CAUSE_STRING, CAUSE_CLASS).orElse(null);

        return container != null
                && container.getInstance().isPresent()
                && container.getInstance().get() instanceof HuskyUI;
    }

    /**
     * Gets the {@link Logger} used by HuskyUI.
     *
     * @return the Logger used by HuskyUI
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}

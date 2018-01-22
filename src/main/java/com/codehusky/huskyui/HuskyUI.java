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

import com.codehusky.huskyui.states.Page;
import com.codehusky.huskyui.states.action.ActionType;
import com.codehusky.huskyui.states.action.runnable.RunnableAction;
import com.codehusky.huskyui.states.action.runnable.UIRunnable;
import com.codehusky.huskyui.states.element.ActionableElement;
import com.codehusky.huskyui.states.element.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.StringProperty;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * The HuskyUI class that gets loaded by Sponge at runtime.
 *
 * <p>Currently this class serves no real purpose except as
 * an easily accessible container for versioning and logging
 * data.</p>
 */
@Plugin(id = HuskyUI.PLUGIN_ID, name = HuskyUI.PLUGIN_NAME, version = HuskyUI.PLUGIN_VERSION,
        description = "A framework for Inventory UI.")
public class HuskyUI {

    @Inject
    private Metrics metrics;

    /**
     * The ID of HuskyUI for Sponge.
     */
    public static final String PLUGIN_ID = "huskyui";

    /**
     * The Name of HuskyUI for Sponge.
     */
    public static final String PLUGIN_NAME = "HuskyUI";

    /**
     * The Version of HuskyUI for Sponge.
     */
    public static final String PLUGIN_VERSION = "0.5.0";

    /**
     * The HuskyUI {@link Logger} used throughout the plugin.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HuskyUI.class);


    private final ElementRegistry registry = new ElementRegistry();

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
    private Cause genericCause;

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
     * Gets the {@link Logger} used by HuskyUI.
     *
     * @return the Logger used by HuskyUI
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     *
     * @return HuskyUI Global Element Registry
     */
    public ElementRegistry getElementRegistry() {
        return registry;
    }

    /*@Listener
    public void serverStart(GameStartedServerEvent event){
        RunnableAction testAction = new RunnableAction(registry, ActionType.NONE,"");
        testAction.setRunnable(context -> {
            StateContainer container = new StateContainer();
            Page testPage = Page.builder()
                    .setTitle(Text.of("WOAH"))
                    .build("testpage");
            container.setInitialState(testPage);
            container.launchFor(context.getObserver());
        });
        ActionableElement testElement = new ActionableElement(
                                            testAction,
                                            ItemStack.builder()
                                                    .itemType(ItemTypes.COMPASS)
                                                    .add(Keys.DISPLAY_NAME, Text.of("COMPASS OWO"))
                                                    .build());

        registry.registerAutoElement(testElement);
    }

    @Listener
    public void reload(GameReloadEvent event){

    }*/

    /**
     * Handle auto-item delivery to player.
     *
     * @param event ClientConnectionEvent Join, when player is created into world.
     */
    @Listener
    public void onPlayerSpawn(ClientConnectionEvent.Join event){
        HashMap<Integer, ItemStack> items = registry.getAutoItems();
        HashMap<Integer, Integer> itemPositions = registry.getAutoItemLocations();
        if(itemPositions.size() > 0) {
            event.getTargetEntity().getInventory().clear();
            int slotNum = 0;
            for (Inventory slot : event.getTargetEntity().getInventory().slots()) {
                if (itemPositions.containsKey(slotNum)) {
                    slot.set(items.get(itemPositions.get(slotNum)));
                }/*else{
                slot.set(ItemStack.builder().itemType(ItemTypes.BARRIER).add(Keys.DISPLAY_NAME,Text.of("SLOTNUM " + slotNum)).build());
            }*/
                slotNum++;
            }
            for(int elementID: items.keySet()){
                if(!itemPositions.containsValue(elementID)){
                    event.getTargetEntity().getInventory().offer(items.get(elementID));
                }
            }
        }else{
            int slotNum = 0;
            for (Inventory slot : event.getTargetEntity().getInventory().slots()) {
                if (slot.peek().isPresent()) {
                    Optional<Integer> eleID = registry.getElementIDFromItemStack(slot.peek().get());
                    if(eleID.isPresent()){
                        slot.clear();
                    }
                }
                slotNum++;
            }
            for(int elementID: items.keySet()){
                if(!itemPositions.containsValue(elementID)){
                    event.getTargetEntity().getInventory().offer(items.get(elementID));
                }
            }
        }

    }

    /**
     * Handler for ActionableElement actions.
     *
     * @param event When a player interacts with an item with their secondary mouse button.
     */
    @Listener(order= Order.LAST)
    public void onElementInteract(InteractItemEvent.Secondary event){
        Optional<Element> ele = registry.getElementFromItemStack(event.getItemStack().createStack());
        if(ele.isPresent()){
            if(ele.get() instanceof ActionableElement) {
                ActionableElement aElement = ((ActionableElement) ele.get()).copy(registry);
                aElement.getAction().setObserver((Player)event.getCause().root());
                aElement.getAction().runAction("");
            }
        }
    }

}

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
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.item.inventory.*;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.StringProperty;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
    public static final String PLUGIN_VERSION = "0.5.1";

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

    public Cause getGenericCause() {
        return genericCause;
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
    @Listener
    public void serverStarting(GameStartedServerEvent event){
        this.genericCause = Cause.of(NamedCause.of("PluginContainer", this.pluginContainer));
    }
    /*@Listener
    public void serverStart(GameStartedServerEvent event){
        RunnableAction testAction = new RunnableAction(registry, ActionType.NONE,"");
        testAction.setRunnable(context -> {
            StateContainer container = new StateContainer();
            Page testPage = Page.builder()
                    .setTitle(Text.of(TextColors.GOLD,"Navigator"))
                    .setAutoPaging(true)
                    .addElement(new Element(
                            ItemStack.builder()
                                .itemType(ItemTypes.DIAMOND)
                                .add(Keys.DISPLAY_NAME,Text.of(TextColors.BLUE,"Diamond Rush"))
                                .build()
                            ))
                    .addElement(new Element(
                            ItemStack.builder()
                                    .itemType(ItemTypes.FIREWORKS)
                                    .add(Keys.DISPLAY_NAME,Text.of(TextColors.RED,"Fireworks Palooza"))
                                    .build()
                    ))
                    .addElement(new Element(
                            ItemStack.builder()
                                    .itemType(ItemTypes.MINECART)
                                    .add(Keys.DISPLAY_NAME,Text.of(TextColors.GRAY,"Roller Coasters"))
                                    .build()
                    ))
                    .build("testpage");
            container.setInitialState(testPage);
            container.launchFor(context.getObserver());
        });
        ActionableElement testElement = new ActionableElement(
                                            testAction,
                                            ItemStack.builder()
                                                    .itemType(ItemTypes.COMPASS)
                                                    .add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD,"Navigator"))
                                                    .build());

        registry.registerAutoElement(4,testElement);
        ItemStack litMC = ItemStack.builder()
                .itemType(ItemTypes.REDSTONE_TORCH)
                .add(Keys.DISPLAY_NAME,Text.of(TextColors.RED,"LitMC"))
                .build();
        registry.registerAutoElement(0,new Element(litMC));
        registry.registerAutoElement(8,new Element(litMC));

        registry.registerAutoElement(new Element(ItemStack.builder().itemType(ItemTypes.MINECART).add(Keys.DISPLAY_NAME,Text.of("movable 1")).build()));
        registry.registerAutoElement(new Element(ItemStack.builder().itemType(ItemTypes.MINECART).add(Keys.DISPLAY_NAME,Text.of("movable 2")).build()));
        registry.registerAutoElement(new Element(ItemStack.builder().itemType(ItemTypes.MINECART).add(Keys.DISPLAY_NAME,Text.of("movable 3")).build()));
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
    public void onElementInteract(InteractItemEvent event){
        Optional<Element> ele = registry.getElementFromItemStack(event.getItemStack().createStack());
        if(ele.isPresent()){
            if(event instanceof InteractItemEvent.Secondary) {
                if (ele.get() instanceof ActionableElement) {
                    ActionableElement aElement = ((ActionableElement) ele.get()).copy(registry);
                    aElement.getAction().setObserver((Player) event.getCause().root());
                    aElement.getAction().runAction("");
                }
            }
            event.setCancelled(true);
        }
    }


    /**
     * Handle item drops
     * @param event dispense event
     */
    @Listener
    public void onItemDrop(DropItemEvent.Dispense event){
        for(Entity e :event.getEntities()){
            if(e instanceof Item){
                ItemStack affectedStack = ((Item) e).getItemData().item().get().createStack();
                Optional<Integer> potentialID = registry.getElementIDFromItemStack(affectedStack);
                if(potentialID.isPresent()){
                    if(registry.elementExists(potentialID.get())){
                        event.setCancelled(true); //NOTHING should drop a registered item. >:(
                        //TODO: handle https://github.com/SpongePowered/SpongeCommon/issues/1678 properly w/ workaround
                    }
                }
            }
        }
    }

    /**
     * Handle item usage
     * @param event useitemstackevent.start
     */
    @Listener
    public void onItemUse(UseItemStackEvent.Start event){
        Optional<Integer> potentialID = registry.getElementIDFromItemStack(event.getItemStackInUse().createStack());
        if(potentialID.isPresent()){
            if(registry.elementExists(potentialID.get())){
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handle inventory clicks
     * @param event clickinvevent
     */
    @Listener(order = Order.PRE)
    public void onItemClick(ClickInventoryEvent event){

        if( event instanceof ClickInventoryEvent.Primary ||
            event instanceof ClickInventoryEvent.Secondary ||
            event instanceof ClickInventoryEvent.Shift||
            event instanceof ClickInventoryEvent.Creative){

            ItemStack affected;
            if(event.getTransactions().isEmpty()){
                System.out.println(event);
            }
            affected= event.getTransactions().get(0).getOriginal().createStack();
            if(event instanceof  ClickInventoryEvent.Shift || (affected.getItem() == ItemTypes.AIR || affected.getItem() == ItemTypes.NONE) ){
                affected = event.getTransactions().get(0).getDefault().createStack();
            }
            Optional<Integer> potentialID = registry.getElementIDFromItemStack(affected);
            if(potentialID.isPresent()){
                if(registry.elementExists(potentialID.get())){
                    if(registry.isElementAuto(potentialID.get())){
                        if(event.getTransactions().get(0).getSlot().parent().getArchetype().equals(InventoryArchetypes.PLAYER)){
                            if(registry.isElementFixedAuto(potentialID.get())) {
                                event.setCancelled(true);
                            }
                        }else{
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


}

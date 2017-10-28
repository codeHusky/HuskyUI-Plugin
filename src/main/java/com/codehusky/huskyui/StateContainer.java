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
import com.codehusky.huskyui.states.State;
import com.google.common.collect.Maps;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.StringProperty;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The StateContainer's purpose is to be the gateway for information
 * regarding any active GUI. It can also be easily copied, with the
 * purpose of assigning a separate container for each user accessing
 * the GUI, allowing extensible on a per-user basis.
 */
public class StateContainer {

    /**
     * Contains the {@link State}s in use by this container, sorted
     * by that State's ID.
     */
    @Nonnull private final Map<String, State> states;

    /**
     * References the ID of the {@link State} which has been
     * deemed as the "default" State.
     *
     * <p>When the GUI is initially opened, for instance, this
     * ID should reference which State to be loaded first.</p>
     */
    @Nullable private String initialState;

    @Nullable private Consumer<Page> pageUpdater;

    @Nullable public Task scheduledTask;

    /**
     * A StateContainer constructor. Left empty to pass equally
     * empty data to the other constructor.
     */
    public StateContainer() {
        this(Maps.newHashMap());
    }

    /**
     * A StateContainer constructor. Passed to it are the current
     * {@link State}s that this GUI can expect to contain.
     *
     * @param states the current States for this GUI
     */
    public StateContainer(@Nonnull final Map<String, State> states) {
        this.states = states;
        this.initialState = null;
        this.pageUpdater = null;
        scheduledTask = null;
    }

    /**
     * A StateContainer constructor. Passed to it are the current
     * {@link State}s that this GUI can expect to contain.
     *
     * <p>Also requires an initial State to be defined.</p>
     *
     * @param states the current States for this GUI
     * @param initialState the initial State
     */
    public StateContainer(@Nonnull final Map<String, State> states, @Nonnull final String initialState) {
        this.states = states;
        this.initialState = initialState;
        this.pageUpdater = null;
        scheduledTask = null;
    }

    /**
     * Gets the current {@link State}s in use by this GUI.
     *
     * @return the current States in use by this GUI
     */
    @Nonnull
    public Map<String, State> getStates() {
        return this.states;
    }

    /**
     * Gets a specific {@link State} in use by this GUI,
     * based on its ID.
     *
     * @param id the ID of the State being requested
     * @return the State being requested; null if non-existent
     */
    @Nullable
    public State getState(@Nonnull final String id) {
        return this.states.get(id);
    }

    /**
     * Determines whether or not the requested {@link State} exists.
     *
     * @param id the ID of the State being determined
     * @return true if the State exists, false otherwise
     */
    public boolean hasState(@Nonnull final String id) {
        return this.getState(id) != null;
    }

    /**
     * Adds a {@link State} to this GUI.
     *
     * If no default state has been set, this will also set the default state.
     *
     * @param state the State to be added
     */
    public void addState(@Nonnull final State state) {
        state.setContainer(this);

        if (this.states.containsKey(state.getId())) {
            throw new IllegalStateException("A State with ID \"" + state.getId() + "\" already exists in this container.");
        }
        if(this.initialState == null){
            this.initialState = state.getId();
        }

        this.states.put(state.getId(), state);
    }

    /**
     * Removes a {@link State} from this GUI.
     *
     * @param id the ID of the State to be removed
     */
    public void removeState(@Nonnull final String id) {
        if (this.initialState != null && this.initialState.equals(id)) {
            this.initialState = null;
        }

        this.states.remove(id);
    }

    /**
     * Removes a {@link State} from this GUI.
     *
     * @param state the State object itself to be removed
     */
    public void removeState(@Nonnull final State state) {
        if (this.hasState(state.getId())) {
            this.removeState(state.getId());
        }
    }


    /**
     * Sets the initial {@link State} to be displayed when
     * the GUI is opened.
     *
     * @param state the State to be displayed when the GUI is opened
     */
    public void setInitialState(@Nonnull final State state) {
        this.initialState = state.getId();

        if (!this.states.containsKey(state.getId())) {
            this.addState(state);
        }
    }

    /**
     * Opens a specific {@link State} for a {@link Player},
     * based on the expected ID of the State.
     *
     * @param player the Player to display the State to
     * @param id the ID of the State being requested
     */
    public void openState(@Nonnull final Player player, @Nonnull final String id) {
        final State state = this.copy().states.get(id);

        if (state == null) {
            fail(player, "Attempted to open a nonexistent state!");
            fail(player, "Invalid ID: " + id);
            InventoryUtil.close(player);
            this.scheduledTask.cancel();
            this.scheduledTask = null;
            return;
        }

        state.setObserver(player);

        if (state instanceof Page) {
            InventoryUtil.close(player);
            Page page = (Page) state;
            Inventory toShow = page.getPageView();
            if(this.scheduledTask != null){
                this.scheduledTask.cancel();
                this.scheduledTask = null;
            }
            if(page.isUpdatable()){
                this.pageUpdater = page.getUpdateConsumer();
                this.scheduledTask = Sponge.getScheduler().createTaskBuilder().execute(() -> {
                    if(page.getObserver().getOpenInventory().isPresent()) {
                        Container container = page.getObserver().getOpenInventory().get();
                        if (container.getProperties(StringProperty.class).size() != 2) {
                            scheduledTask.cancel();
                            scheduledTask = null;
                            return;
                        }else{
                            StringProperty property1 = ((StringProperty)container.getProperties(StringProperty.class).toArray()[0]);
                            StringProperty property2 = ((StringProperty)container.getProperties(StringProperty.class).toArray()[1]);
                            if(!property2.getValue().equals(page.getId())) {
                                if (!property1.getValue().equals(page.getId())) {
                                    scheduledTask.cancel();
                                    scheduledTask = null;
                                    return;
                                }
                            }
                        }
                    }
                    if(page.getActualTicks() % page.getUpdateTickRate() == 0) {
                        this.pageUpdater.accept(page);
                    }
                    page.tickIncrement();

                }).intervalTicks(1).submit(HuskyUI.getInstance());
            }
            InventoryUtil.open(player, toShow);

            return;
        }

        InventoryUtil.close(player);
        this.scheduledTask.cancel();
        this.scheduledTask = null;
        fail(player, "Attempted to open an invalid or incomplete state!");
        fail(player, "Invalid ID: " + id);
    }

    /**
     * Opens the initial {@link State} for the {@link Player}.
     *
     * @param player the Player to show the initial State to
     */
    public void launchFor(@Nonnull final Player player) {
        if (this.initialState == null) {
            fail(player, "Attempted to open a container without an initial state!");
            return;
        }

        this.openState(player, this.initialState);
    }

    /**
     * Creates a copy of this StateContainer.
     *
     * @return a copy of this StateContainer
     */
    @Nonnull
    public StateContainer copy() {
        final StateContainer container = new StateContainer();

        for (final State state : this.states.values()) {
            container.addState(state.copy(container));
        }

        if (this.initialState != null) {
            container.setInitialState(states.get(this.initialState));
        }

        return container;
    }

    /**
     * A simple convenience method to send failures to
     * {@link Player}s; added by a misguided, lazy developer.
     *
     * @param player the Player to send the message to
     * @param message the message to be sent to the Player
     */
    private static void fail(@Nonnull final Player player, @Nonnull final String message) {
        player.sendMessage(Text.of(TextColors.RED, message));
    }
}

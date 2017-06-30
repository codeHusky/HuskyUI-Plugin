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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * StateContainer is a system to make "GUIs" easy to make and interact with for developers. Specifically me.
 * This class actually doesn't do anything other than hold data for GUIs.
 */
public class StateContainer {

    @Nonnull private final Map<String, State> states;
    @Nullable private String initialState;

    public StateContainer() {
        this(Maps.newHashMap());
    }

    public StateContainer(@Nonnull final Map<String, State> states) {
        this.states = states;
        this.initialState = null;
    }

    public StateContainer(@Nonnull final Map<String, State> states, @Nonnull final String initialState) {
        this.states = states;
        this.initialState = initialState;
    }

    @Nonnull
    public Map<String, State> getStates() {
        return this.states;
    }

    @Nullable
    public State getState(@Nonnull final String id) {
        return this.states.get(id);
    }

    public boolean hasState(@Nonnull final String id) {
        return this.getState(id) != null;
    }

    public void addState(@Nonnull final State state) {
        state.setContainer(this);

        if (this.states.containsKey(state.getId())) {
            throw new IllegalStateException("A State with ID \"" + state.getId() + "\" already exists in this container.");
        }

        this.states.put(state.getId(), state);
    }

    public void removeState(@Nonnull final String id) {
        if (this.initialState != null && this.initialState.equals(id)) {
            this.initialState = null;
        }

        this.states.remove(id);
    }

    public void removeState(@Nonnull final State state) {
        if (this.hasState(state.getId())) {
            this.removeState(state.getId());
        }
    }

    public void setInitialState(@Nonnull final State state) {
        this.initialState = state.getId();

        if (!this.states.containsKey(state.getId())) {
            this.addState(state);
        }
    }

    public void openState(@Nonnull final Player player, @Nonnull final String id) {
        final State state = this.copy().states.get(id);

        if (state == null) {
            fail(player, "Attempted to open a nonexistent state!");
            fail(player, "Invalid ID: " + id);
            player.closeInventory(HuskyUI.getInstance().getGenericCause());
            return;
        }

        state.setObserver(player);

        if (state instanceof Page) {
            player.openInventory(((Page) state).generatePageView(), HuskyUI.getInstance().getGenericCause());
            return;
        }

        player.closeInventory(HuskyUI.getInstance().getGenericCause());
        fail(player, "Attempted to open an invalid or incomplete state!");
        fail(player, "Invalid ID: " + id);
    }

    public void launchFor(@Nonnull final Player player) {
        if (this.initialState == null) {
            fail(player, "Attempted to open a container without an initial state!");
            return;
        }

        this.openState(player, this.initialState);
    }

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

    private static void fail(@Nonnull final Player player, @Nonnull final String message) {
        player.sendMessage(Text.of(TextColors.RED, message));
    }
}

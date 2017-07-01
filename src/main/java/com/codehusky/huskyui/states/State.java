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

package com.codehusky.huskyui.states;

import com.codehusky.huskyui.StateContainer;
import org.spongepowered.api.entity.living.player.Player;
import javax.annotation.Nonnull;

/**
 * A State in the GUI process. Typically extended by {@link Page}.
 */
public class State {

    /**
     * The ID of this State.
     */
    private final String id;

    /**
     * The ID of the parent to this State.
     */
    private String parent;

    /**
     * The {@link StateContainer} responsible for this State.
     */
    private StateContainer container;

    /**
     * The {@link Player}, if applicable, currently viewing this GUI.
     */
    private Player observer;

    /**
     * An easily created way to construct a State.
     *
     * <p>This constructor will assume that the ID is "null",
     * and should really only be used for testing purposes.</p>
     */
    public State() {
        this("null");
    }

    /**
     * A State constructor. Creates a State to be, typically,
     * stored in a {@link StateContainer}.
     *
     * @param id the ID of this State
     */
    public State(@Nonnull final String id) {
        this.id = id;
        this.parent = null;
        this.container = null;
        this.observer = null;
    }

    /**
     * Gets the ID of this State.
     *
     * @return the ID of this State
     */
    public String getId() {
        return this.id;
    }

    /**
     * Gets the ID of the parent to this State.
     *
     * @return the ID of the parent to this State
     */
    public String getParent() {
        return this.parent;
    }

    /**
     * Determines whether or not this State has a parent.
     *
     * @return true if this State has a parent; false otherwise
     */
    public boolean hasParent() {
        return this.parent != null;
    }

    /**
     * Sets the ID of the parent to this State.
     *
     * @param parent the ID of the parent to this State
     */
    public void setParent(final String parent) {
        if(parent == null) return;
        this.parent = parent;
    }

    /**
     * Gets the {@link StateContainer} responsible for this State.
     *
     * @return the StateContainer responsible for this State
     */
    public StateContainer getContainer() {
        return this.container;
    }

    /**
     * Sets the {@link StateContainer} responsible for this State.
     *
     * @param container the StateContainer responsible for this State
     */
    public void setContainer(@Nonnull final StateContainer container) {
        this.container = container;
    }

    /**
     * Determines whether or not this State has a {@link StateContainer}.
     *
     * <p>Typically this would always be false, but people are crazy
     * so I dunno.</p>
     *
     * @return true if this State has a StateContainer; false otherwise
     */
    public boolean isHeadless() {
        return this.container == null;
    }

    /**
     * Gets the {@link Player} currently viewing this State, if applicable.
     *
     * @return the {@link Player} currently viewing this State
     */
    public Player getObserver() {
        return this.observer;
    }

    /**
     * Determines whether or not this State is being viewed.
     *
     * @return true if this State is being viewed; false otherwise
     */
    public boolean hasObserver() {
        return this.observer != null;
    }

    /**
     * Sets the {@link Player} who is viewing this State.
     *
     * @param observer the Player viewing this State
     */
    public void setObserver(final Player observer) {
        this.observer = observer;
    }

    /**
     * Creates a copy of this State.
     *
     * @param newContainer the {@link StateContainer} that will be taking
     *                     responsibility for this State.
     * @return a copy of this State
     */
    @Nonnull
    public State copy(@Nonnull final StateContainer newContainer) {
        final State state = new State(this.id);

        state.setContainer(newContainer);
        state.setObserver(this.observer);
        state.setParent(this.parent);

        return state;
    }
}

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

package com.codehusky.huskyui.states.action;

import com.codehusky.huskyui.HuskyUI;
import com.codehusky.huskyui.InventoryUtil;
import com.codehusky.huskyui.StateContainer;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;

/**
 * This class' purpose is to be used to determine the movement
 * of a user within a GUI.
 * @see CommandAction
 * @see com.codehusky.huskyui.states.action.runnable.RunnableAction
 */
public class Action {

    /**
     * The {@link StateContainer} that is responsible for this Action.
     */
    @Nonnull private final StateContainer container;

    /**
     * The {@link Player} observing the {@link com.codehusky.huskyui.states.Page}
     * that this action is being performed on.
     */
    private Player observer = null;

    /**
     * The type of Action taking place.
     */
    @Nonnull private final ActionType type;

    /**
     * The intended {@link com.codehusky.huskyui.states.State} that the
     * observer should find themselves in when this Action finishes.
     */
    @Nonnull private final String goalState;

    /**
     * Constructs an Action.
     *
     * @param container the {@link StateContainer} that is responsible for this Action
     * @param type the type of Action taking place
     * @param goalState the intended {@link com.codehusky.huskyui.states.State}
     *                  for the observer to land
     */
    public Action(@Nonnull final StateContainer container,
                  @Nonnull final ActionType type,
                  @Nonnull final String goalState) {
        this.container = container;
        this.type = type;
        this.goalState = goalState;
    }

    /**
     * Gets the {@link StateContainer} that is responsible for this Action.
     *
     * @return the StateContainer responsible for this Action
     */
    @Nonnull
    public StateContainer getContainer() {
        return this.container;
    }

    /**
     * The {@link Player} that is observing this Action.
     *
     * @return the Player observing this Action
     */
    public Player getObserver() {
        return this.observer;
    }

    /**
     * Sets the {@link Player} that is observing this Action
     *
     * @param observer the Player that is observing this Action
     */
    public void setObserver(Player observer) {
        this.observer = observer;
    }

    /**
     * Gets the type of Action to be performed.
     *
     * @return the type of Action to be performed
     */
    @Nonnull
    public ActionType getType() {
        return this.type;
    }

    /**
     * Gets the intended {@link com.codehusky.huskyui.states.State} for
     * the {@link Player} to end up in after this Action is performed.
     *
     * @return the intended State for the Player to end up in
     */
    @Nonnull
    public String getGoalState() {
        return this.goalState;
    }

    /**
     * Performs this Action.
     *
     * @param currentState the current State before the Action is performed
     */
    public void runAction(@Nonnull final String currentState) {
        switch (this.type) {
            case CLOSE:
                InventoryUtil.close(this.observer);
                break;
            case BACK:
                if (this.container.hasState(currentState)) {
                    if (this.container.getState(currentState).hasParent()) {
                        this.container.openState(this.observer, this.container.getState(currentState).getParent());
                    } else {
                        this.observer.playSound(SoundTypes.BLOCK_ANVIL_LAND, this.observer.getLocation().getPosition(), 0.5);
                        if(container.scheduledTask != null){
                            container.scheduledTask.cancel();
                            container.scheduledTask = null;
                        }
                        InventoryUtil.close(this.observer);
                        this.observer.sendMessage(Text.of(TextColors.RED, "Impossible BACK action - closing broken State."));
                    }
                }
                break;
            case NORMAL:
                this.container.openState(this.observer, this.goalState);
                break;
            case NONE:
                // do nothing
                break;
            default:
                this.observer.sendMessage(Text.of("??"));
                break;
        }
    }

    /**
     * Creates a copy of this Action.
     *
     * @param newContainer the new {@link StateContainer} to be responsible for this new Action
     * @return a copy of this Action
     */
    @Nonnull
    public Action copy(@Nonnull final StateContainer newContainer) {
        return new Action(newContainer, this.type, this.goalState);
    }
}

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

package com.codehusky.huskyui.states.action.runnable;

import com.codehusky.huskyui.StateContainer;
import com.codehusky.huskyui.states.action.Action;
import com.codehusky.huskyui.states.action.ActionType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A separate type of {@link Action} which performs developer-defined
 * actions separate from navigating the GUI.
 */
public class RunnableAction extends Action {

    /**
     * The UIRunnable that determines this action to be taken.
     */
    @Nullable private UIRunnable runnable;

    /**
     * Constructs a new RunnableAction without a pre-determined action.
     *
     * @param container the {@link StateContainer} responsible for this RunnableAction
     * @param type the type of {@link Action} being performed
     * @param goalState the destination for a {@link org.spongepowered.api.entity.living.player.Player}
     *                  after this Action is completed
     */
    public RunnableAction(@Nonnull final StateContainer container,
                          @Nonnull final ActionType type,
                          @Nonnull final String goalState) {
        this(container, type, goalState, null);
    }

    /**
     * Constructs a new RunnableAction with a pre-determined action.
     *
     * @param container the {@link StateContainer} responsible for this RunnableAction
     * @param type the type of {@link Action} being performed
     * @param goalState the destination for a {@link org.spongepowered.api.entity.living.player.Player}
     *                  after this Action is completed
     * @param runnable the additional Action to be performed
     */
    public RunnableAction(@Nonnull final StateContainer container,
                          @Nonnull final ActionType type,
                          @Nonnull final String goalState,
                          @Nullable final UIRunnable runnable) {
        super(container, type, goalState);
        this.runnable = runnable;
    }

    /**
     * Gets the additional action to be performed.
     *
     * @return the additional actions to be performed
     */
    @Nullable
    public UIRunnable getRunnable() {
        return this.runnable;
    }

    /**
     * Sets the additional actions to be performed.
     *
     * @param runnable the additional actions to be performed
     */
    public void setRunnable(@Nonnull final UIRunnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Runs the additional actions.
     *
     * @param currentState the current State before the Action is performed
     */
    @Override
    public void runAction(@Nonnull final String currentState, Inventory inventory) {
        if (this.runnable != null) {
            this.runnable.run(this);
        } else {
            this.getObserver().sendMessage(Text.of(TextColors.RED, "Cannot run a null action!"));
        }
        super.runAction(currentState,inventory);
    }

    /**
     * Creates a copy of this RunnableAction.
     *
     * @param newContainer the new {@link StateContainer} to be responsible for this new Action
     * @return a copy of this RunnableAction
     */
    @Nonnull
    @Override
    public RunnableAction copy(@Nonnull final StateContainer newContainer) {
        // UIRunnable doesn't need to be copied - it's just an action.
        return new RunnableAction(newContainer, this.getType(), this.getGoalState(), this.runnable);
    }
}

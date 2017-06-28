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
import com.codehusky.huskyui.states.StateContainer;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import javax.annotation.Nonnull;

/**
 * This is something that the State handler will use in the occurrence that this is called to decide
 * where to move the current GUI Instance to.
 */
public class Action {

    @Nonnull private final StateContainer container;
    @Nonnull private final Player observer;
    @Nonnull private final ActionType type;
    @Nonnull private final String goalState;

    public Action(@Nonnull final StateContainer container,
                  @Nonnull final Player observer,
                  @Nonnull final ActionType type,
                  @Nonnull final String goalState) {
        this.container = container;
        this.observer = observer;
        this.type = type;
        this.goalState = goalState;
    }

    @Nonnull
    public StateContainer getContainer() {
        return this.container;
    }

    @Nonnull
    public Player getObserver() {
        return this.observer;
    }

    @Nonnull
    public ActionType getType() {
        return this.type;
    }

    @Nonnull
    public String getGoalState() {
        return this.goalState;
    }

    public void runAction(@Nonnull final String currentState) {
        if (this.type == ActionType.CLOSE) {
            this.observer.closeInventory(HuskyUI.getInstance().getGenericCause());
        } else if (this.type == ActionType.BACK) {
            if (this.container.hasState(currentState)) {
                if (this.container.getState(currentState).hasParent()) {
                    this.container.openState(this.observer, this.container.getState(currentState).getParent());
                } else {
                    this.observer.playSound(SoundTypes.BLOCK_ANVIL_LAND, this.observer.getLocation().getPosition(), 0.5);
                    this.observer.closeInventory(HuskyUI.getInstance().getGenericCause());
                    this.observer.sendMessage(Text.of(TextColors.RED, "Impossible BACK action - closing broken State."));
                }
            } else {
                this.observer.sendMessage(Text.of(TextColors.RED, "Cannot travel non-existent state."));
                this.observer.sendMessage(Text.of(TextColors.RED, "Invalid ID: " + currentState));
            }
        } else {
            // ActionType == ActionType.NORMAL
            this.container.openState(this.observer, this.goalState);
        }
    }
}

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
import com.codehusky.huskyui.StateContainer;
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
    private Player observer = null;
    @Nonnull private final ActionType type;
    @Nonnull private final String goalState;

    public Action(@Nonnull final StateContainer container,
                  @Nonnull final ActionType type,
                  @Nonnull final String goalState) {
        this.container = container;
        this.type = type;
        this.goalState = goalState;
    }




    @Nonnull
    public StateContainer getContainer() {
        return this.container;
    }

    public Player getObserver() {
        return this.observer;
    }

    public void setObserver(Player observer) {
        this.observer = observer;
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
        switch (this.type) {
            case CLOSE:
                this.observer.closeInventory(HuskyUI.getInstance().getGenericCause());
                break;
            case BACK:
                if (this.container.hasState(currentState)) {
                    if (this.container.getState(currentState).hasParent()) {
                        this.container.openState(this.observer, this.container.getState(currentState).getParent());
                    } else {
                        this.observer.playSound(SoundTypes.BLOCK_ANVIL_LAND, this.observer.getLocation().getPosition(), 0.5);
                        this.observer.closeInventory(HuskyUI.getInstance().getGenericCause());
                        this.observer.sendMessage(Text.of(TextColors.RED, "Impossible BACK action - closing broken State."));
                    }
                }
                break;
            case NORMAL:
                this.container.openState(this.observer, this.goalState);
                break;
        }
    }

    @Nonnull
    public Action copy(@Nonnull final StateContainer newContainer) {
        return new Action(newContainer, this.type, this.goalState);
    }
}

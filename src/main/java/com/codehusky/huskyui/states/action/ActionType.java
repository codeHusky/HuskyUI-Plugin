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

/**
 * The type of {@link Action} to be performed.
 */
public enum ActionType {

    /**
     * Triggered if a {@link org.spongepowered.api.entity.living.player.Player}
     * is going "Back" to the parent.
     */
    BACK,

    /**
     * Triggered if a {@link org.spongepowered.api.entity.living.player.Player}
     * is closing the {@link org.spongepowered.api.item.inventory.Inventory}.
     */
    CLOSE,

    /**
     * Triggered for a normal action, usually forward.
     */
    NORMAL,

    /**
     * This is a false flag operation. We've been swindled!
     * Quick, scramble the jets!
     */
    NONE
}

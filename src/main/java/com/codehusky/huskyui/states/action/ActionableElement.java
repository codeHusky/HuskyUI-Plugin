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

import com.codehusky.huskyui.states.StateContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemAction extends Action {

    @Nullable private ItemStack item;

    private ItemAction(@Nonnull final StateContainer container,
                       @Nonnull final Player observer,
                       @Nonnull final ActionType type,
                       @Nonnull final String goalState,
                       @Nonnull final ItemStack item) {
        super(container, observer, type, goalState);

        this.item = item;
    }

    @Nullable
    public ItemStack getItem() {
        return this.item;
    }

    public void setItem(@Nonnull final ItemStack item) {
        this.item = item;
    }
}

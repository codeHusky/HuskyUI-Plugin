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
 * along with HuskyUI. If not, see <http://www.gnu.org/licenses/>.
 */

package com.codehusky.huskyui.inventory.action.context;

import com.codehusky.huskyui.api.action.context.ActionContext;
import com.codehusky.huskyui.api.action.context.ObserverContext;
import com.codehusky.huskyui.inventory.InventoryPage;
import org.spongepowered.api.entity.living.player.Player;
import javax.annotation.Nonnull;
import java.util.UUID;

public class InventoryActionContext implements ActionContext<InventoryObserverContext> {

    @Nonnull private final UUID page;
    @Nonnull private final InventoryObserverContext observer;

    public InventoryActionContext(@Nonnull final UUID page, @Nonnull final InventoryObserverContext observer) {
        this.page = page;
        this.observer = observer;
    }

    @Nonnull
    @Override
    public UUID getPage() {
        return this.page;
    }

    @Nonnull
    @Override
    public InventoryObserverContext getObserver() {
        return this.observer;
    }
}

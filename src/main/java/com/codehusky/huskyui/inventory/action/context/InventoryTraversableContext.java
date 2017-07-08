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

import com.codehusky.huskyui.api.action.context.TraversableContext;
import com.codehusky.huskyui.api.action.context.TraversalType;
import com.codehusky.huskyui.inventory.InventoryPage;
import org.spongepowered.api.entity.living.player.Player;
import javax.annotation.Nonnull;
import java.util.UUID;

public class InventoryTraversableContext extends InventoryActionContext implements TraversableContext<InventoryObserverContext> {

    @Nonnull private final UUID oldPage;
    @Nonnull private final TraversalType traversalType;

    public InventoryTraversableContext(@Nonnull final UUID page,
                                       @Nonnull final UUID oldPage,
                                       @Nonnull final InventoryObserverContext observer,
                                       @Nonnull final TraversalType traversalType) {
        super(page, observer);

        this.oldPage = oldPage;
        this.traversalType = traversalType;
    }

    @Nonnull
    @Override
    public UUID getOldPage() {
        return this.oldPage;
    }

    @Nonnull
    @Override
    public TraversalType getTraversalType() {
        return this.traversalType;
    }
}

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

package com.codehusky.huskyui.inventory;

import com.codehusky.huskyui.api.Page;
import org.spongepowered.api.item.inventory.ItemStack;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class InventoryPage implements Page<InventoryPage, ItemStack> {

    @Nonnull private final UUID identifier;
    @Nonnull private final Set<InventoryPage> children;
    @Nonnull private final ItemStack menuVisibleItem;

    public InventoryPage(@Nonnull final UUID identifier,
                         @Nonnull final Set<InventoryPage> children,
                         @Nonnull final ItemStack menuVisibleItem) {
        this.identifier = identifier;
        this.children = children;
        this.menuVisibleItem = menuVisibleItem;
    }

    @Nonnull
    @Override
    public UUID getIdentifier() {
        return this.identifier;
    }

    @Nonnull
    @Override
    public Collection<InventoryPage> getChildren() {
        return this.children;
    }

    @Nullable
    @Override
    public InventoryPage getChild(@Nonnull final UUID identifier) {
        return this.children.stream()
                .filter(child -> child.getIdentifier().equals(identifier))
                .findFirst().orElse(null);
    }

    @Override
    public void addChild(@Nonnull final InventoryPage child) {
        if (this.hasChild(child.getIdentifier())) {
            throw new IllegalStateException("Two inventories with the same identifier cannot share the same parent.");
        }

        this.children.add(child);
    }

    @Override
    public void addChild(@Nonnull final UUID child) {

    }

    @Override
    public void removeChild(@Nonnull final InventoryPage child) {
        this.children.remove(child);
    }

    @Override
    public void removeChild(@Nonnull final UUID identifier) {
        this.children.remove(this.getChild(identifier));
    }

    @Override
    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    @Override
    public boolean hasChild(@Nonnull final UUID identifier) {
        return this.children.stream()
                .anyMatch(child -> child.getIdentifier().equals(identifier));
    }

    @Nonnull
    @Override
    public ItemStack getMenuSelector() {
        return this.menuVisibleItem;
    }
}

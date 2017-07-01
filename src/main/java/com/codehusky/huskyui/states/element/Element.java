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

package com.codehusky.huskyui.states.element;

import com.codehusky.huskyui.StateContainer;
import org.spongepowered.api.item.inventory.ItemStack;
import javax.annotation.Nonnull;

/**
 * A basic Element is, essentially, a simple wrapper for
 * an {@link ItemStack} and serves no purpose.
 */
public class Element {

    /**
     * The {@link ItemStack} being wrapped.
     */
    @Nonnull private final ItemStack item;

    /**
     * Constructs a new Element.
     *
     * @param item the {@link ItemStack} to be wrapped
     */
    public Element(@Nonnull final ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the {@link ItemStack} being wrapped.
     *
     * @return the wrapped ItemStack
     */
    @Nonnull
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Creates a copy of this Element.
     *
     * @param newContainer the {@link StateContainer} now
     *                     responsible for this Element
     * @return a copy of this Element
     */
    @Nonnull
    public Element copy(@Nonnull final StateContainer newContainer) { // We don't use this on purpose.
        return new Element(this.item.copy());
    }
}

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
import com.codehusky.huskyui.states.action.Action;
import com.codehusky.huskyui.states.action.CommandAction;
import org.spongepowered.api.item.inventory.ItemStack;
import javax.annotation.Nonnull;

/**
 * An extension of {@link Element} that also wraps
 * an {@link Action} to be used, typically, by an event.
 */
public class ActionableElement extends Element {

    /**
     * The {@link Action} to be performed on
     * on this Element.
     */
    @Nonnull private Action action;

    /**
     * Constructs a new ActionableElement.
     *
     * @param action the {@link Action} to be performed
     * @param item the {@link ItemStack} this {@link Element} wraps
     */
    public ActionableElement(@Nonnull final Action action, @Nonnull final ItemStack item) {
        super(item);
        this.action = action;
    }

    /**
     * Gets the {@link Action} to be performed on this Element.
     *
     * @return the Action to be performed on this Element
     */
    @Nonnull
    public Action getAction() {
        return this.action;
    }

    /**
     * Creates a copy of this ActionableElement.
     *
     * @param newContainer the {@link StateContainer} now
     *                     responsible for this ActionableElement
     * @return a copy of this ActionableElement
     */
    @Nonnull
    @Override
    public ActionableElement copy(@Nonnull final StateContainer newContainer) {
        return new ActionableElement(this.action.copy(newContainer), this.getItem().copy());
    }
}

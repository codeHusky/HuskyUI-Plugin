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

package com.codehusky.huskyui.api.action;

import com.codehusky.huskyui.api.action.context.TraversableContext;
import javax.annotation.Nonnull;

public interface Traversable<C extends TraversableContext> extends Actionable<C> {

    @Override
    void act(@Nonnull final C context);
}

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

package com.codehusky.huskyui.api.action.context;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface ActionContext<O extends ObserverContext> {

    @Nonnull
    UUID getPage();

    @Nonnull
    O getObserver();
}

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

package com.codehusky.huskyui.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

public interface Page<P extends Page, T> {

    @Nonnull
    UUID getIdentifier();

    @Nonnull
    Collection<P> getChildren();

    @Nullable
    P getChild(@Nonnull final UUID identifier);

    void addChild(@Nonnull final P child);

    void addChild(@Nonnull final UUID child);

    void removeChild(@Nonnull final P child);

    void removeChild(@Nonnull final UUID identifier);

    boolean hasChildren();

    boolean hasChild(@Nonnull final UUID identifier);

    @Nonnull
    T getMenuSelector();
}

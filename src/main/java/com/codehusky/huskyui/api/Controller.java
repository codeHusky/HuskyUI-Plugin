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

import com.codehusky.huskyui.api.action.context.ObserverContext;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public interface Controller<M extends Menu, P extends Page, O extends ObserverContext> {

    Map<UUID, M> getMenuRegistry();

    M getMenu(@Nonnull final UUID identifier);

    Map<UUID, P> getPageRegistry();

    P getPage(@Nonnull final UUID identifier);

    Map<UUID, O> getObserverRegistry();

    @Nonnull O getObserver(@Nonnull final UUID identifier);

    void add(@Nonnull final M menu);

    void add(@Nonnull final P page);

    void add(@Nonnull final O observer);

    void drawMenu(@Nonnull final O observer, @Nonnull final M menu);

    void drawMenu(@Nonnull final O observer, @Nonnull final UUID identifier);

    void drawMenu(@Nonnull final UUID observer, @Nonnull final M menu);

    void drawMenu(@Nonnull final UUID observer, @Nonnull final UUID identifier);

    void drawPage(@Nonnull final O observer, @Nonnull final P page);

    void drawPage(@Nonnull final O observer, @Nonnull final UUID identifier);

    void drawPage(@Nonnull final UUID observer, @Nonnull final P page);

    void drawPage(@Nonnull final UUID observer, @Nonnull final UUID identifier);
}

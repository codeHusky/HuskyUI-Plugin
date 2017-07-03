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

import com.codehusky.huskyui.api.Controller;
import com.codehusky.huskyui.inventory.action.context.InventoryObserverContext;
import com.google.common.collect.Maps;
import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;

public final class InventoryController implements Controller<InventoryMenu, InventoryPage, InventoryObserverContext> {

    private static final InventoryController INSTANCE = new InventoryController();
    public static InventoryController getInstance() {
        return INSTANCE;
    }

    @Nonnull private final Map<UUID, InventoryMenu> menuRegistry;
    @Nonnull private final Map<UUID, InventoryPage> pageRegistry;
    @Nonnull private final Map<UUID, InventoryObserverContext> observerRegistry;

    private InventoryController() {
        this.menuRegistry = Maps.newHashMap();
        this.pageRegistry = Maps.newHashMap();
        this.observerRegistry = Maps.newHashMap();
    }

    @Nonnull
    @Override
    public Map<UUID, InventoryMenu> getMenuRegistry() {
        return this.menuRegistry;
    }

    @Override
    public InventoryMenu getMenu(@Nonnull final UUID identifier) {
        return this.menuRegistry.get(identifier);
    }

    @Nonnull
    @Override
    public Map<UUID, InventoryPage> getPageRegistry() {
        return this.pageRegistry;
    }

    @Override
    public InventoryPage getPage(@Nonnull final UUID identifier) {
        return this.pageRegistry.get(identifier);
    }

    @Nonnull
    @Override
    public Map<UUID, InventoryObserverContext> getObserverRegistry() {
        return this.observerRegistry;
    }

    @Nonnull
    @Override
    public InventoryObserverContext getObserver(@Nonnull final UUID identifier) {
        if (this.observerRegistry.containsKey(identifier)) {
            return this.observerRegistry.get(identifier);
        }

        final InventoryObserverContext context = new InventoryObserverContext(identifier);
        this.observerRegistry.put(identifier, context);
        return context;
    }

    @Override
    public void add(@Nonnull final InventoryMenu menu) {
        this.menuRegistry.put(menu.getIdentifier(), menu);
    }

    @Override
    public void add(@Nonnull final InventoryPage page) {
        this.pageRegistry.put(page.getIdentifier(), page);
    }

    @Override
    public void add(@Nonnull final InventoryObserverContext observer) {
        this.observerRegistry.put(observer.getPlayerIdentifier(), observer);
    }

    @Override
    public void drawMenu(@Nonnull final InventoryObserverContext observer, @Nonnull final InventoryMenu menu) {
        this.drawPage(observer, this.getPage(menu.getEntryPoint()));
    }

    @Override
    public void drawMenu(@Nonnull final InventoryObserverContext observer, @Nonnull final UUID identifier) {
        this.drawMenu(observer, this.getMenu(identifier));
    }

    @Override
    public void drawMenu(@Nonnull final UUID observer, @Nonnull final InventoryMenu menu) {
        this.drawMenu(this.getObserver(observer), menu);
    }

    @Override
    public void drawMenu(@Nonnull final UUID observer, @Nonnull final UUID identifier) {
        this.drawMenu(this.getObserver(observer), this.getMenu(identifier));
    }

    @Override
    public void drawPage(@Nonnull final InventoryObserverContext observer, @Nonnull final InventoryPage page) {

    }

    @Override
    public void drawPage(@Nonnull final InventoryObserverContext observer, @Nonnull final UUID identifier) {
        this.drawPage(observer, this.getPage(identifier));
    }

    @Override
    public void drawPage(@Nonnull final UUID observer, @Nonnull final InventoryPage page) {
        this.drawPage(this.getObserver(observer), page);
    }

    @Override
    public void drawPage(@Nonnull final UUID observer, @Nonnull final UUID identifier) {
        this.drawPage(this.getObserver(observer), this.getPage(identifier));
    }
}

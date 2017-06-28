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

package com.codehusky.huskyui.states;

import org.spongepowered.api.entity.living.player.Player;
import javax.annotation.Nonnull;

public class State {

    private final String id;
    private String parent;
    private StateContainer container;
    private Player observer;

    public State() {
        this("null");
    }

    public State(@Nonnull final String id) {
        this.id = id;
        this.parent = null;
        this.container = null;
        this.observer = null;
    }

    public String getId() {
        return this.id;
    }

    public String getParent() {
        return this.parent;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public void setParent(@Nonnull final String parent) {
        this.parent = parent;
    }

    public StateContainer getContainer() {
        return this.container;
    }

    public void setContainer(@Nonnull final StateContainer container) {
        this.container = container;
    }

    public boolean isHeadless() {
        return this.container == null;
    }

    public Player getObserver() {
        return this.observer;
    }

    public boolean hasObserver() {
        return this.observer != null;
    }

    public void setObserver(@Nonnull final Player observer) {
        this.observer = observer;
    }
}

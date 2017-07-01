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

package com.codehusky.huskyui.states.action.runnable;

import javax.annotation.Nonnull;

/**
 * A pre-determined action to be run when interfacing with a Page.
 */
public interface UIRunnable {

    /**
     * The additional actions to be run when interfacing with a Page.
     *
     * @param context the RunnableAction that relates to this runnable
     */
    void run(@Nonnull final RunnableAction context);
}

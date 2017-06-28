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

package com.codehusky.huskyui.components;

import org.spongepowered.api.entity.living.player.Player;

public class RunnableAction extends Action {

    private UIRunnable runnable;

    public RunnableAction(StateContainer gui, Player observer, boolean isCloseAction, boolean isBackAction, String goalState) {
        super(gui, observer, isCloseAction, isBackAction, goalState);
    }

    public void setRunnable(UIRunnable runnable){
        this.runnable = runnable;
    }
    @Override
    public void runAction(String currentState){
        runnable.run(this);
    }
}

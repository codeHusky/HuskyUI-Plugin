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

import com.codehusky.huskyui.HuskyUI;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * This is something that the State handler will use in the occurrence that this is called to decide
 * where to move the current GUI Instance to.
 */
public class Action {
    public StateContainer gui;
    public Player observer;
    public boolean isCloseAction;
    public boolean isBackAction;
    public String goalState;
    public Action(StateContainer gui, Player observer, boolean isCloseAction, boolean isBackAction, String goalState){
        this.gui = gui;
        this.observer = observer;
        this.isCloseAction = isCloseAction;
        this.isBackAction=isBackAction;
        this.goalState = goalState;
    }

    public void runAction(String currentState){
        //fired when action is activated
        if(isCloseAction)
            observer.closeInventory(HuskyUI.instance.genericCause);
        else if(isBackAction) {
            if(gui.getState(currentState).hasParent) {
                gui.openState(observer, gui.getState(currentState).parentState);
            }else{
                observer.playSound(SoundTypes.BLOCK_ANVIL_LAND,observer.getLocation().getPosition(),0.5);
                observer.closeInventory(HuskyUI.instance.genericCause);
                observer.sendMessage(Text.of(TextColors.RED,"Impossible back action, closing broken state."));
            }
        }else{
            //normal state change
            //observer.closeInventory(HuskyCrates.instance.genericCause);

            gui.openState(observer,goalState);
        }
    }
}

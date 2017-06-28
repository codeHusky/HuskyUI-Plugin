package com.codehusky.huskyui.components;

import com.codehusky.huskyui.HUIPlugin;
import com.codehusky.huskyui.HuskyUI;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * This is something that the State handler will use in the occurance that this is called to decide
 * where to move the current GUI Instance to.
 */
public class Action {
    private HuskyUI gui;
    private Player observer;
    private boolean isCloseAction;
    private boolean isBackAction;
    private String goalState;


    public Action(HuskyUI gui, Player observer, boolean isCloseAction, boolean isBackAction, String goalState) {
        this.gui = gui;
        this.observer = observer;
        this.isCloseAction = isCloseAction;
        this.isBackAction = isBackAction;
        this.goalState = goalState;
    }

    public void runAction(String currentState) {
        //fired when action is activated
        if (isCloseAction)
            observer.closeInventory(HUIPlugin.getInstance().getGenericCause());
        else if (isBackAction) {
            if (gui.getState(currentState).hasParent()) {
                gui.openState(observer, gui.getState(currentState).getParentState());
            } else {
                observer.playSound(SoundTypes.BLOCK_ANVIL_LAND, observer.getLocation().getPosition(), 0.5);
                observer.closeInventory(HUIPlugin.getInstance().getGenericCause());
                observer.sendMessage(Text.of(TextColors.RED, "Impossible back action, closing broken state."));
            }
        } else {
            //normal state change
            //observer.closeInventory(HuskyCrates.instance.genericCause);

            gui.openState(observer, goalState);
        }
    }

    public HuskyUI getGui() {
        return gui;
    }

    public Player getObserver() {
        return observer;
    }

    public String getGoalState() {
        return goalState;
    }

    public boolean isBackAction() {
        return isBackAction;
    }

    public boolean isCloseAction() {
        return isCloseAction;
    }
}

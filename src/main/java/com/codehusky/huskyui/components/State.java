package com.codehusky.huskyui.components;

import com.codehusky.huskyui.HuskyUI;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by lokio on 6/25/2017.
 */
public class State {
    private String id;
    private boolean hasParent = false;
    private String parentState;
    private HuskyUI ui = null;
    private Player observer = null;

    public State() {
        this("null");
    }

    public State(String id) {
        this.id = id;
    }

    public void setParent(String parent) {
        parentState = parent;
        hasParent = true;
    }

    public Player getObserver() {
        return observer;
    }

    public HuskyUI getUi() {
        return ui;
    }

    public String getId() {
        return id;
    }

    public String getParentState() {
        return parentState;
    }

    public boolean hasParent() {
        return hasParent;
    }
}

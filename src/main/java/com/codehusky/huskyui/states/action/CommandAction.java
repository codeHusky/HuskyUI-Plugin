package com.codehusky.huskyui.states.action;

import com.codehusky.huskyui.StateContainer;
import com.codehusky.huskyui.states.State;
import org.spongepowered.api.Sponge;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class CommandAction extends Action {
    @Nonnull private final String command;
    @Nonnull private CommandReceiver receiver;

    /**
     * Constructs an Action.
     *
     * @param container the {@link StateContainer} that is responsible for this Action
     * @param type      the type of Action taking place
     * @param goalState the intended {@link State}
     */
    public CommandAction(@Nonnull StateContainer container, @Nonnull ActionType type, @Nonnull String goalState, @Nonnull final String command) {
        super(container, type, goalState);
        this.command = command;
        this.receiver = CommandReceiver.SERVER;
    }

    public CommandAction(@Nonnull StateContainer container, @Nonnull ActionType type, @Nonnull String goalState,
                         @Nonnull final String command, @Nonnull CommandReceiver receiver) {
        super(container, type, goalState);
        this.command = command;
        this.receiver = receiver;
    }

    @Override
    public void runAction(@Nonnull String currentState) {
        Sponge.getCommandManager().process(this.receiver == CommandReceiver.SERVER ?
            Sponge.getServer().getConsole() : getObserver(), this.command);
        super.runAction(currentState);
    }

    @Nonnull
    public String getCommand() {
        return command;
    }

    @Nonnull
    public CommandReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(@Nonnull CommandReceiver receiver) {
        this.receiver = receiver;
    }

    public enum CommandReceiver {
        PLAYER,
        SERVER
    }

    /**
     * Creates a copy of this CommandAction.
     *
     * @param newContainer the new {@link StateContainer} to be responsible for this new Action
     * @return a copy of this Action
     */
    @Nonnull
    public Action copy(@Nonnull final StateContainer newContainer) {
        return new CommandAction(newContainer, this.getType(), this.getGoalState(),command,receiver);
    }
}

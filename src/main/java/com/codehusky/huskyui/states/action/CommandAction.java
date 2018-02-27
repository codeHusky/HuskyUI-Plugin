package com.codehusky.huskyui.states.action;

import com.codehusky.huskyui.StateContainer;
import com.codehusky.huskyui.states.State;
import org.spongepowered.api.Sponge;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
/**
 * This class is used to handle situations requiring command execution by an item.
 * You should not use this class to open another UI state container, use a {@link com.codehusky.huskyui.states.action.runnable.RunnableAction} for that.
 */
public class CommandAction extends Action {
    @Nonnull private final String command;
    @Nonnull private CommandReceiver receiver;

    /**
     * Constructs a CommandAction.
     *
     * @param container the {@link StateContainer} that is responsible for this Action
     * @param type      the type of Action taking place
     * @param goalState the intended {@link State}
     * @param command   The command intended to run
     */
    public CommandAction(@Nonnull StateContainer container, @Nonnull ActionType type, @Nonnull String goalState, @Nonnull final String command) {
        super(container, type, goalState);
        this.command = command;
        this.receiver = CommandReceiver.SERVER;
    }

    /**
     * Constructs a CommandAction.
     *
     * @param container the {@link StateContainer} that is responsible for this Action
     * @param type      the type of Action taking place
     * @param goalState the intended {@link State}
     * @param command   the command intended to run
     * @param receiver  the {@link CommandReceiver} intended to run the command
     */
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

    /**
     * Set the {@link CommandReceiver} of the CommandAction
     *
     * @param receiver   the {@link CommandReceiver} that will replace the previously specified one.
     */
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

package commandutils;

import java.util.concurrent.ExecutionException;

/**
 * This interface should be implemented into any command and you should register that command via the CommandManager.
 */
public interface Command {

    /**
     * @return The identifier for the command
     */
    public String getName();

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    public String getDescription();

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    public String getUsage();

    /**
     * @return A list of aliases for the command
     */
    public String[] getAliases();

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    public void onCommand(CommandContext ctx) throws ExecutionException, InterruptedException;

    /**
     * Checks another command for equality
     *
     * @param command Command to check against
     * @return Whether the commands have the same identifier
     */
    public default boolean equals(Command command) {
        return this.getName().equals(command.getName());
    }
}


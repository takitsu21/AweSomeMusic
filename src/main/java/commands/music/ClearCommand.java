package commands.music;

import commandutils.Command;
import commandutils.CommandContext;
import util.lavaplayer.PlayerManager;

public class ClearCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "clear";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Clear all the tracks added before";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "clear";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[]{"c"};
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild()).scheduler.getQueue().clear();
        ctx.getChannel().sendMessage("All tracks has been removed.").queue();
    }
}

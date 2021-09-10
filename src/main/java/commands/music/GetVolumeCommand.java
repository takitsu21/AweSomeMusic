package commands.music;

import commandutils.Command;
import commandutils.CommandContext;
import util.lavaplayer.PlayerManager;

public class GetVolumeCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "volume";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Get volume of the bot";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "volume";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[]{"v"};
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        ctx.getChannel().sendMessage(String.format(
                        "Current volume `%s`", PlayerManager.getINSTANCE()
                                .getMusicManager(ctx.getGuild())
                                .audioPlayer
                                .getVolume()))
                .queue();
    }
}

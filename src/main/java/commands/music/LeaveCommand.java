package commands.music;

import commandutils.Command;
import commandutils.CommandContext;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "leave";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Leave current voice channel";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "leave";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[]{"l"};
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            audioManager.closeAudioConnection();
        } else {
            ctx.getChannel().sendMessage("I'm not in a voice channel!").queue();
        }
    }
}

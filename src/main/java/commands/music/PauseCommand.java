package commands.music;

import commandutils.Command;
import commandutils.CommandContext;
import util.lavaplayer.GuildMusicManager;
import util.lavaplayer.PlayerManager;

public class PauseCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "pause";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Pause the current track.";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "pause";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[0];
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
        if (!ctx.getGuild().getAudioManager().isConnected()) {
            ctx.getChannel().sendMessage("The bot isn't connected in any channel yet!").queue();
            return;
        }

        if (guildMusicManager.audioPlayer.getPlayingTrack() == null) {
            ctx.getChannel().sendMessage("No song running for the moment!").queue();
            return;
        }
        guildMusicManager.audioPlayer.setPaused(!guildMusicManager.audioPlayer.isPaused());
    }
}

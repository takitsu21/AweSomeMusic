package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commandutils.Command;
import commandutils.CommandContext;
import util.lavaplayer.GuildMusicManager;
import util.lavaplayer.PlayerManager;

public class TrackCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "track";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "View current track running";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "track";
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
        AudioTrack currentTrack = guildMusicManager.audioPlayer.getPlayingTrack();
        if (currentTrack != null)  {
            ctx.getChannel().sendMessage(String.format("Current track `%s` by `%s` duration : `%s`",
                    currentTrack.getInfo().title,
                    currentTrack.getInfo().author,
                    currentTrack.getDuration()))
                    .queue();
        } else {
            ctx.getChannel().sendMessage("No sound running!").queue();
        }

    }
}

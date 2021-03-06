package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commandutils.Command;
import commandutils.CommandContext;
import util.lavaplayer.PlayerManager;
import util.lavaplayer.TrackHelper;
import util.lavaplayer.TrackScheduler;

public class NextTrackCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "next";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Move to the next track";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "next";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[]{"skip"};
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        TrackScheduler scheduler = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild()).scheduler;
        if (scheduler.getQueue().isEmpty()) {
            ctx.getChannel().sendMessage("No tracks in the queue!").queue();
            return;
        }
        AudioTrack currentTrack = scheduler.getQueue().peek();
        if (currentTrack != null) {
            ctx.getChannel().sendMessageEmbeds(TrackHelper.generateEmbedPlayingSong(ctx, currentTrack)).queue();
            scheduler.nextTrack();
        } else {
            ctx.getChannel().sendMessage("Queue is empty!").queue();
        }


    }
}

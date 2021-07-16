package commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commandutils.Command;
import commandutils.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import util.lavaplayer.GuildMusicManager;
import util.lavaplayer.PlayerManager;

public class QueueCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "queue";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Get queue tracks";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "queue";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[]{"q"};
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
        ctx.getChannel().sendMessageEmbeds(generateQueueEmbed(ctx, guildMusicManager)).queue();
    }

    private MessageEmbed generateQueueEmbed(CommandContext ctx, GuildMusicManager guildMusicManager) {
        StringBuilder sb = new StringBuilder();
        int acc = 1;
        for (AudioTrack audioTrack : guildMusicManager.scheduler.getQueue()) {
            sb.append("`")
                    .append(acc)
                    .append(" - ")
                    .append(audioTrack.getInfo().title)
                    .append(" by ")
                    .append(audioTrack.getInfo().author)
                    .append("`\n");
            if (acc == 25) {
                break;
            }
            acc++;
        }
        AudioTrack currentTrack = guildMusicManager.scheduler.getQueue().peek();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setThumbnail(ctx.getSelfUser().getAvatarUrl());
        embed.setAuthor("Queue songs",
                currentTrack != null ? currentTrack.getInfo().uri : null,
                ctx.getSelfUser().getAvatarUrl());
        embed.setDescription(sb.toString());
        embed.setFooter(
                currentTrack != null ? currentTrack.getDuration() + " left" : null,
                ctx.getSelfUser().getAvatarUrl());
        return embed.build();
    }
}

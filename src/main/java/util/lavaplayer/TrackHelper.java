package util.lavaplayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commandutils.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class TrackHelper {
    public static String readableHumanTime(long duration) {
        long seconds = duration / 1000;
        return String.format("%02dh:%02dm:%02ds time left", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    public static MessageEmbed generateEmbedPlayingSong(CommandContext ctx, AudioTrack track) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Color.RED);
        embed.setAuthor("\uD83C\uDFB6 Adding to queue \uD83C\uDFB6", track.getInfo().uri, ctx.getMember().getUser().getAvatarUrl());
        embed.addField("Title", track.getInfo().title, false);
        embed.addField("Author", track.getInfo().author, false);

        embed.setThumbnail("http://img.youtube.com/vi/" + track.getInfo().identifier + "/0.jpg");
        if (track.getInfo().isStream && track.getInfo().uri.contains("twitch")) {
            embed.setFooter("\uD83D\uDD34 Live Stream", ctx.getSelfUser().getAvatarUrl());
            embed.addField("Platform", "<:twitch:885584876030533682>", false);
            embed.setColor(new Color(0x6441a5));
        } else if (track.getInfo().isStream) {
            embed.setFooter("\uD83D\uDD34 Live Stream", ctx.getSelfUser().getAvatarUrl());
            embed.addField("Platform", "<:youtube:885583568804384779>", false);
        } else {
            embed.setFooter(TrackHelper.readableHumanTime(track.getDuration()), ctx.getSelfUser().getAvatarUrl());
            embed.addField("Platform", "<:youtube:885583568804384779>", false);
        }
        return embed.build();
    }
}

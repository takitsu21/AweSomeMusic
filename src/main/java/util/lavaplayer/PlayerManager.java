package util.lavaplayer;

import bot.Logger;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commandutils.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static PlayerManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), (guilId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(CommandContext ctx, String musicUrl) {
        TextChannel channel = ctx.getChannel();
        GuildMusicManager musicManager = getMusicManager(channel.getGuild());


        audioPlayerManager.loadItemOrdered(musicManager, musicUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
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
                channel.sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                channel.sendMessage(String.format("Adding playlist `%s` (`%d` songs)",
                                playlist.getName(),
                                playlist.getTracks().size()))
                        .queue();
                for (AudioTrack track : playlist.getTracks()) {
                    musicManager.scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                // Notify the user that we've got nothing
                channel.sendMessage("We couldn't find any music with that name sorry!").queue();
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                // Notify the user that everything exploded
                channel.sendMessage("An error occured! " + throwable.getMessage()).queue();
                Logger.error(throwable.getMessage());
            }
        });
    }
}

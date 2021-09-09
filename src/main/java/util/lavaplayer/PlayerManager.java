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
import java.util.concurrent.TimeUnit;

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
                long seconds = track.getDuration() / 1000;
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.RED);
                embed.setAuthor("\uD83C\uDFB6 Adding to queue \uD83C\uDFB6", track.getInfo().uri, ctx.getMember().getUser().getAvatarUrl());
                embed.addField("Title", track.getInfo().title, true);
                embed.addField("Author", track.getInfo().author, true);
                embed.setThumbnail("http://img.youtube.com/vi/" + track.getInfo().identifier + "/0.jpg");
                embed.setFooter(String.format("%02dh:%02dm:%02ds time left", seconds / 3600, (seconds % 3600) / 60, seconds % 60), ctx.getSelfUser().getAvatarUrl());
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

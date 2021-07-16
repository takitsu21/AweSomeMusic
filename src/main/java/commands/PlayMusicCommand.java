package commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commandutils.Command;
import commandutils.CommandContext;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import util.lavaplayer.AudioPlayerSendHandler;
import util.lavaplayer.PlayerManager;
import util.lavaplayer.TrackScheduler;

import java.util.Arrays;
import java.util.Objects;

public class PlayMusicCommand implements Command {
    // The identifier for the command is "example"
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Play music";
    }

    // This command has no arguments.
    @Override
    public String getUsage() {
        return "!play <Song Name>";
        // return "example <required arg> [optional arg]";
    }

    // This command has no aliases
    @Override
    public String[] getAliases() {
        return new String[]{"p"};
        // return new String[]{"alias1", "alias2"};
    }

    // For this command all we do is reply to tell the member that he ran the command.
    @Override
    public void onCommand(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member member = Objects.requireNonNull(ctx.getMember());
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (memberVoiceState != null && !memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("You need to be in a voice channel to use this command.").queue();
            return;
        }

        AudioManager audioManager = connect(ctx);

        PlayerManager.getINSTANCE().loadAndPlay(ctx.getChannel(), ctx.getArgs()[1]);
    }

    private AudioManager connect(CommandContext ctx) {
        VoiceChannel vc = ctx.getVoiceChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            ctx.getChannel().sendMessage("The bot is already trying to connect! Enter the chill zone!").queue();
            return null;
        }
        audioManager.openAudioConnection(vc);
        return audioManager;
    }
}

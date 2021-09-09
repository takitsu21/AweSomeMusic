package commands.music;

import commandutils.Command;
import commandutils.CommandContext;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.json.JSONArray;
import org.json.JSONObject;
import util.http.HttpHelper;
import util.lavaplayer.PlayerManager;

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
        VoiceChannel vc = ctx.getVoiceChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        if (!audioManager.isConnected()) {
            audioManager.openAudioConnection(vc);
            audioManager.setSelfDeafened(true);
        }
        if (!ctx.getArgs()[1].startsWith("http")) {
            HttpHelper helper = new HttpHelper();
            try {
                JSONObject json = helper.getResponseJson("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=2&order=viewCount&q=" + String.join(" ", ctx.getArgs()) + "&key=AIzaSyDuc3lvdWNcYAdOZJk-PiXqpk7f4pYUxz8");
                JSONArray musicInfo = !json.isEmpty() ? (JSONArray) json.get("items") : null;
                if (musicInfo == null) {
                    channel.sendMessage("The song cannot be found!").queue();
                } else {
                    JSONObject firstItem = (JSONObject) musicInfo.get(0);
                    String videoId = ((String)((JSONObject)firstItem.get("id")).get("videoId"));
                    PlayerManager.getINSTANCE().loadAndPlay(ctx, "https://www.youtube.com/watch?v=" + videoId);
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
                channel.sendMessage("The song cannot be found!").queue();
            }
        } else {
            PlayerManager.getINSTANCE().loadAndPlay(ctx, ctx.getArgs()[1]);
        }

    }
}

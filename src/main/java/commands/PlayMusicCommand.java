package commands;

import commandutils.Command;
import commandutils.CommandContext;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

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
        VoiceChannel vc = ctx.getVoiceChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        if (audioManager.isConnected()) {
            ctx.getChannel().sendMessage("The bot is already trying to connect! Enter the chill zone!").queue();
            return;
        }
        audioManager.openAudioConnection(vc);
        ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + " I'm connected to your voice channel"
                + vc.getName()).queue();

    }
}

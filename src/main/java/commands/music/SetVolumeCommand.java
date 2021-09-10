package commands.music;

import commandutils.Command;
import commandutils.CommandContext;
import util.lavaplayer.GuildMusicManager;
import util.lavaplayer.PlayerManager;

public class SetVolumeCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "setvolume";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Set new volume for the bot";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "setvolume <New Volume>";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[]{"setv"};
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
        if (ctx.getArgs().length != 2) {
            ctx.getChannel().sendMessage("Wrong parameter").queue();
            return;
        }
        ctx.getChannel().sendMessage(String.format("New volume from `%s` to `%s`",
                        guildMusicManager.audioPlayer.getVolume(),
                        ctx.getArgs()[1]))
                .queue();
        guildMusicManager.audioPlayer.setVolume(Integer.parseInt(ctx.getArgs()[1]));
    }
}

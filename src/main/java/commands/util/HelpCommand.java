package commands.util;

import commandutils.Command;
import commandutils.CommandContext;
import commandutils.CommandManager;
import net.dv8tion.jda.api.EmbedBuilder;

public class HelpCommand implements Command {
    /**
     * @return The identifier for the command
     */
    @Override
    public String getName() {
        return "help";
    }

    /**
     * This can be used with {@link #getUsage()} to create a help command
     *
     * @return The description of the command
     */
    @Override
    public String getDescription() {
        return "Show bot commands";
    }

    /**
     * This cn be used with {@link #getDescription()} to create a help command.
     * Note: when displaying the usage, You should add the command prefix at that point and not have this method return with the prefix.
     *
     * @return the usage of the command
     */
    @Override
    public String getUsage() {
        return "help";
    }

    /**
     * @return A list of aliases for the command
     */
    @Override
    public String[] getAliases() {
        return new String[]{"h"};
    }

    /**
     * Runs the command that this command represents
     *
     * @param ctx The context in which to run the command
     */
    @Override
    public void onCommand(CommandContext ctx) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Help command", ctx.getMember().getUser().getAvatarUrl());
        embed.setFooter(null, ctx.getSelfUser().getAvatarUrl());
        for (Command command : CommandManager.getCommands()) {
            embed.addField(String.format("%s`%s`", CommandManager.getPrefix(), command.getUsage()), command.getDescription(), false);
        }
        ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
    }
}

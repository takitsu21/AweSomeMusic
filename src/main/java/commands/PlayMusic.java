package commands;

import commandutils.Command;
import commandutils.CommandContext;
import commandutils.CommandManager;

public class PlayMusic implements Command {
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
        return new String[0];
        // return new String[]{"alias1", "alias2"};
    }

    // For this command all we do is reply to tell the member that he ran the command.
    @Override
    public void onCommand(CommandContext context) {
        context.getChannel().sendMessage(context.getMember().getAsMention() + " You ran the command: " + context.getLabel()).queue();

    }
}

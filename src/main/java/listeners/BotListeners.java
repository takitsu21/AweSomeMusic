package listeners;

import commandutils.CommandContext;
import commandutils.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotListeners extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String prefix = CommandManager.getPrefix();
        if (!e.getMessage().getContentRaw().startsWith(prefix)) {
            return;
        }
        String command = e.getMessage().getContentStripped().replace(prefix, "").split(" ")[0];
        String[] args = e.getMessage().getContentRaw().replace(prefix, "").replace(command, "").split(" ");
        CommandContext ctx = new CommandContext(e.getMember(), e.getChannel(), e.getMessage(), command, args);

        CommandManager.runCommand(ctx);

    }
}

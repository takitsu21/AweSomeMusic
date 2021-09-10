package listeners;

import commandutils.CommandContext;
import commandutils.CommandManager;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import util.db.MongoDBManager;

import java.util.concurrent.ExecutionException;

public class EventsListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String prefix = CommandManager.getPrefix();
        if (!e.getMessage().getContentRaw().startsWith(prefix) || e.getAuthor().isBot()) {
            return;
        }
        String command = e.getMessage().getContentStripped().replace(prefix, "").split(" ")[0];
        String[] args = e.getMessage().getContentRaw().replace(prefix, "").replace(command, "").split(" ");
        CommandContext ctx = new CommandContext(e, command, args);
        try {
            CommandManager.runCommand(ctx);
        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onDisconnect(@NotNull DisconnectEvent event) {
        MongoDBManager.getClient().close();
        System.out.println("Bot disconnected");
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        MongoDBManager.getClient().close();
        System.out.println("Bot disconnected");
    }
}

import commands.PlayMusic;
import commandutils.Command;
import commandutils.CommandManager;
import listeners.BotListeners;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.logging.Logger;

public class AweSomeBot {
    private static DefaultShardManagerBuilder client;

    public static void main(String[] args) throws LoginException, MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }

        client = DefaultShardManagerBuilder.createDefault(args[0]);
        client.setActivity(Activity.playing("JDA testing"));
        configureMemoryUsage(client);
        client.addEventListeners(new BotListeners());
        client.build();


    }

    private void loadCommands() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        File dir = new File("./src/main/java/commands/");
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            String className = file.getName().split(".java")[0];
            Class<?> c = Class.forName("commands." + className);
            CommandManager.registerCommand((Command) c.newInstance());
        }
    }

    private void unloadCommands() throws ClassNotFoundException {
        for (Command command : CommandManager.getCommands()) {
            CommandManager.unregisterCommand(command);
        }

    }

    public static DefaultShardManagerBuilder getClient() {
        return client;
    }

    public static void configureMemoryUsage(DefaultShardManagerBuilder builder) {
        // Disable cache for member activities (streaming/games/spotify)
        builder.disableCache(CacheFlag.ACTIVITY);

        // Only cache members who are either in a voice channel or owner of the guild
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));

        // Disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);

        // Disable presence updates and typing events
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        builder.setLargeThreshold(50);
    }
}

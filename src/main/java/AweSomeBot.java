import commandutils.Command;
import commandutils.CommandManager;
import listeners.EventsListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Objects;

public class AweSomeBot {
    private static DefaultShardManagerBuilder client;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }

        client = DefaultShardManagerBuilder.createDefault(args[0]);
        try {
            prepareAndBuildClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prepareAndBuildClient() throws ClassNotFoundException, InstantiationException, IllegalAccessException, LoginException {
        client.setActivity(Activity.playing("JDA testing"));
        configureMemoryUsage(client);
        client.addEventListeners(new EventsListener());
        client.build();
        loadCommands();
    }

    /**
     * @return DefaultShardManagerBuilder client
     */
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

    /**
     * Load every commands
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static void loadCommands() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        File dir = new File("./src/main/java/commands/");
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            String className = file.getName().split(".java")[0];
            Class<?> c = Class.forName("commands." + className);
            CommandManager.registerCommand((Command) c.newInstance());
        }
    }

    /**
     * Unload every commands
     */
    private static void unloadCommands() {
        for (Command command : CommandManager.getCommands()) {
            CommandManager.unregisterCommand(command);
        }

    }
}

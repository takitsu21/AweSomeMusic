package bot;

import commandutils.Command;
import commandutils.CommandManager;
import listeners.EventsListener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import util.config.ConfigContext;
import util.config.ReadPropertyFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AweSomeBot {
    private static DefaultShardManagerBuilder client;
    private static ConfigContext cfctx;

    public static void main(String[] args) throws IOException {
//        if (args.length < 1) {
//            System.out.println("You have to provide a token as first argument!");
//            System.exit(1);
//        }
//        Map<String, String> env = System.getenv();
//        System.out.println(env);
        cfctx = new ConfigContext(new ReadPropertyFile());
        client = DefaultShardManagerBuilder.createDefault(cfctx.getBOT_TOKEN());
        try {
            prepareAndBuildClient();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(MongoDBManager.getCollection("awesomebot", "settings"));
//        MongoCollection<Document> collection = MongoDBManager.getCollection("awesomebot", "settings");
////        SingleResultCallback<Document> guildPrefix = (document, throwable) -> {
////            System.out.println(" toJson " + document.toJson());
////        };
//
//        long v = Long.parseLong("812662025934995476");
////        System.out.println(doc.map);
//        Publisher<Document> doc = collection.find(Filters.eq("guild_id", v)).first();
//
//        System.out.println(Objects.requireNonNull(Mono.from(doc).block()).toJson());

    }

    public static ConfigContext getCfctx() {
        return cfctx;
    }

    private static void prepareAndBuildClient() {
        try {
            client.setActivity(Activity.playing("$help | Alpha testing"));
            configureMemoryUsage(client);
            client.addEventListeners(new EventsListener());
            client.build();
            loadCommands(new File("./src/main/java/commands/"), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        MongoCollection<Document> collection = MongoDBManager.getCollection("awesomebot", "settings");
//
//        long v = Long.parseLong("812662025934995476");
//        Publisher<Document> guildPrefix = collection.find(Filters.eq("guild_id", v).first();
//
//        final Mono<Document> getPrefix = Mono.from(guildPrefix)
//                .map(document -> document)
////                .map(MongoDBManager::getCollection)
//                .doOnSuccess(consumer -> {
//                    if (consumer == null) {
//                        System.out.println("error");
//                    }
//                })
//                .defaultIfEmpty(Document.parse("!"))
//                .doOnSubscribe(__ -> {
//                    collection.insertOne(new Document("guild_id", 65465).append("prefix", "!"));
//                    System.out.println("inserted");
//                });
//        System.out.println(getPrefix);
//        System.out.println(Objects.requireNonNull(getPrefix.block()).toJson());
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
        builder.enableCache(CacheFlag.VOICE_STATE);

        // Only cache members who are either in a voice channel or owner of the guild
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));

        // Disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);

        // Disable presence updates and typing events
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
        builder.enableIntents(GatewayIntent.GUILD_VOICE_STATES);

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        builder.setLargeThreshold(50);
    }

    /**
     * @throws URISyntaxException
     */
    private static void loadCommands(File dir, String currentDir) throws URISyntaxException {

        String ressource = AweSomeBot.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        try {
            JarFile jarFile = new JarFile(ressource);

            for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                JarEntry entry = entries.nextElement();
                String file = entry.getName();
                if (file.startsWith("commands") && file.endsWith(".class")) {
                    String classname = file.replace('/', '.').substring(0, file.length() - 6);
                    try {
                        Class<?> c = Class.forName(classname);
                        CommandManager.registerCommand((Command) c.getDeclaredConstructor().newInstance());
                    } catch (Throwable e) {
                        System.out.println("WARNING: failed to instantiate " + classname + " from " + file);
                    }
                }
            }
        } catch (Exception ignored) {
            try {
//                File dir = new File("./src/main/java/commands/");
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.isDirectory()) {
                        loadCommands(new File(file.getAbsolutePath()), file.getName());
                    } else {
                        String className = file.getName().split(".java")[0];
                        Class<?> c = Class.forName("commands." + currentDir + "." + className);
                        CommandManager.registerCommand((Command) c.getDeclaredConstructor().newInstance());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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

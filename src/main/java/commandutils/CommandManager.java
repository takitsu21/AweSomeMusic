package commandutils;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import util.db.MongoDBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    // This will be the prefix required in front of all the commandutils.
    private static final String PREFIX = "!";

    // This is a list of all the Commands that are registered.
    private static List<Command> commands = new ArrayList<>();

    /**
     * Runs a command based on the context. (Context contains the command to run, If command isn't valid it wont run a command)
     *
     * @param context This is the context in which to run the command
     */
    public static void runCommand(CommandContext context) {

        Command commandToRun = null;

        for (Command command : commands) {

            if (context.getLabel().equalsIgnoreCase(command.getName())) {
                commandToRun = command;
            }
            for (String alias : command.getAliases()) {
                if (context.getLabel().equalsIgnoreCase(alias)) {
                    commandToRun = command;
                }
            }

        }

        if (commandToRun == null) {
            return;
        }

        commandToRun.onCommand(context);

    }

    /**
     * Checks if a command with the same identifier is registered.
     *
     * @param command Command to check.
     * @return Whether or not the given command is registered.
     */
    private static boolean commandExists(Command command) {
        for (Command existingCommand : commands) {
            if (command.equals(existingCommand)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Registers the command. (Checks with {@link #commandExists(Command)})
     *
     * @param command The command to register
     */
    public static void registerCommand(Command command) {
        if (!commandExists(command)) {
            commands.add(command);
        }
    }

    /**
     * Attempts to unregister a command will unregister any command that is equal to it from {@link Command#equals(Command)}.
     *
     * @param command Command to unregister
     */
    public static void unregisterCommand(Command command) {
        if (commandExists(command)) {
            for (int i = 0; i < commands.size(); i++) {
                if (commands.get(i).equals(command)) {
                    commands.remove(i);
                    return;
                }
            }
        }
    }


    public static String getPrefix() {
        return PREFIX;
    }

    public static String getPrefix(Guild guild) {
        MongoCollection<Document> collection = MongoDBManager.getCollection("awesomebot", "settings");
        Map<Long, String> cache = new HashMap<>();
        collection.find(Filters.eq("guild_id", guild.getIdLong())).first()
                .subscribe(new Subscriber<Document>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        System.out.println("on subscribe");
                        subscription.request(1);

                    }

                    @Override
                    public void onNext(Document document) {
                        System.out.println("on next");
                        if (document != null) {
                            cache.put(guild.getIdLong(), document.toJson());
                        }


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("query complete");
                    }
                });


//               ;
        System.out.println(cache);
        System.out.println(cache.get(guild.getIdLong()));

        if (cache.get(guild.getIdLong()) == null) {
            collection.insertOne(new Document("guild_id", guild.getIdLong()).append("prefix", PREFIX));
        }
        return cache.get(guild.getIdLong()) != null ? cache.get(guild.getIdLong()) : PREFIX;
    }


    /**
     * Gets a list of all the registered commandutils
     *
     * @return A list of all the registered commandutils.
     */
    public static List<Command> getCommands() {
        return commands;
    }

}


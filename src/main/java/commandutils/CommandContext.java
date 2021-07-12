package commandutils;


import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

/**
 * This class contains context about a command that is run.
 */
public class CommandContext {

    // The Member that ran the command
    private GuildMessageReceivedEvent e;

    // The label used to call the command
    private String label;

    // The args passed with the command
    private String[] args;

    /**
     * @param e       Event received
     * @param label   The label used to call the command
     * @param args    The args passed with the command
     */
    public CommandContext(GuildMessageReceivedEvent e, String label, String[] args) {
        this.e = e;
        this.label = label;
        this.args = args;
    }

    /**
     * @return The Member that ran the command
     */
    public Member getMember() {
        return e.getMember();
    }

    public Guild getGuild() {
        return e.getGuild();
    }

    /**
     * @return The TextChannel the command was ran in
     */
    public TextChannel getChannel() {
        return e.getChannel();
    }

    public VoiceChannel getVoiceChannel() {
        return Objects.requireNonNull(getMember().getVoiceState()).getChannel();
    }

    /**
     * @return The Message that was sent containing the command
     */
    public Message getMessage() {
        return e.getMessage();
    }

    /**
     * @return The label used to call the command
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return The args passed with the command
     */
    public String[] getArgs() {
        return args;
    }

}

